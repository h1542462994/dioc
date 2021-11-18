package org.tty.dioc.core.launcher

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.ConfigModule
import org.tty.dioc.config.schema.ConfigSchema
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.ProvidersSchema
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.ApplicationStartup
import org.tty.dioc.core.CoreModule
import org.tty.dioc.core.basic.*
import org.tty.dioc.core.internal.ComponentStorageImpl
import org.tty.dioc.core.internal.ProviderResolverImpl
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.observable.channel.observe
import org.tty.dioc.util.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

/**
 * kernel loader for constructing [ApplicationContext]
 */
class KernelLoader {
    /**
     * use [ComponentStorageImpl] as the [ComponentStorage]
     */
    private val componentStorage: ComponentStorage = ComponentStorageImpl()

    private val providerResolver: ProviderResolver = ProviderResolverImpl(componentStorage)

    /**
     * entry point to the [ApplicationContext]
     */
    lateinit var entryPoint: ApplicationStartup

    /**
     * add internal component to [ComponentStorage]
     */
    private inline fun <reified T: Any> add(name: String, component: T) {
        componentStorage.addInternalComponent(name, component)
    }

    /**
     * add provider
     */
    private inline fun <reified T : Any> addProvider() {
        val configSchemas = componentStorage.configSchemas
        val providerResolver = componentStorage.providerResolver
        val providerSchema: ProvidersSchema<T> = configSchemas.getDefaultProvider(T::class)
        add<T>(providerSchema.name, providerResolver.resolveProvider(providerSchema.name))
    }

    /**
     * add the [ComponentStorage] to [ComponentStorage] (self add.)
     */
    private fun addComponentStorage() {
        add(ComponentKeys.componentStorage, componentStorage)
    }

    /**
     * add [ConfigSchemas]
     */
    private fun addConfigSchemas() {
        add(ComponentKeys.configSchemas, ConfigSchemas())
    }

    /**
     * load the modules and register [ConfigSchema] to [ConfigSchemas]
     */
    private fun moduleInit() {
        add(ComponentKeys.configModule, ConfigModule(configSchemas = componentStorage.configSchemas))
        add(ComponentKeys.coreModule, CoreModule(configSchemas = componentStorage.configSchemas))
    }

    /**
     * add [ProviderResolver]
     */
    private fun addProviderResolver() {
        add(ComponentKeys.providerResolver, providerResolver)
    }

    /**
     * set [ApplicationStartup]
     */
    fun setApplicationEntryPoint(applicationStartup: ApplicationStartup): KernelLoader {
        this.entryPoint = applicationStartup
        add(ComponentKeys.entryPoint, entryPoint)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun load(): ApplicationContext {
        // ----------------- create basic suites ----------------------

        addComponentStorage()
        addConfigSchemas()
        // TODO: 2021/11/10 支持模块的自动发现。
        moduleInit()

        // -----> after default configuration is load.

        // TODO: 2021/11/10 将解析Provider委托到ComponentResolver
        addProviderResolver()
        // TODO: 2021/11/10 支持从注解和文件中获取配置，支持匿名配置。
        addProvider<ApplicationConfig>()

        // ------------------ create informal suites -------------------

        addProvider<Logger>()
        addProvider<ScopeFactory>()
        addProvider<ScopeAbility>()
        addProvider<ComponentDeclares>()
        addProvider<ComponentResolver>()

        // call on configuration
        entryPoint.onConfiguration(componentStorage.applicationConfig)
        val componentDeclares = componentStorage.componentDeclares

        addProvider<ComponentDeclareScanner>()
        addProvider<EntryPointLoader>()

        // add root components
        // call on startup
        entryPoint.onStartUp(
            componentDeclares
        )


        return object: ApplicationContext {
            val storage = this@KernelLoader.componentStorage
            val declares = storage.componentDeclares
            val scopeAbility = storage.getInternalComponent(CoreModule.scopeAbilitySchema)

            override fun <T : Any> getComponent(indexType: KClass<T>): T {
                return if (indexType.hasAnnotation<InternalComponent>()) {
                    // TODO("当前通过ComponentResolver去创建InternalComponent.")
                    requireNotNull(storage.findComponent(indexType)) {
                        "internalComponent $indexType is not existed."
                    }
                } else {
                    storage
                        .getInternalComponent(CoreModule.componentResolverSchema)
                        .resolve(declare = componentDeclares.singleIndexType(indexType = indexType))
                }
            }

            override fun <T : Any> getComponent(name: String, indexType: KClass<T>): T {
                TODO("Not yet implemented")
            }

            override fun scopeAbility(): ScopeAbility {
                return scopeAbility
            }

            override fun onInit() {
                declares.forEach {
                    if (!it.isLazyComponent && it.lifecycle == Lifecycle.Singleton) {
                        getComponent(it.indexTypes[0])
                    }
                }

                scopeAbility.createChannel.observe(this::onCreateScope)
                scopeAbility.removeChannel.observe(this::onRemoveScope)
                declares.createLazyChannel.observe(this::onCreateLazy)
            }

            /**
             * the function callback after create a lazy service.
             */
            private fun onCreateLazy(createLazy: ComponentDeclares.CreateLazy) {
                val (declarationType, lifecycle, lazy) = createLazy
                if (!lazy) {
                    if (lifecycle == Lifecycle.Singleton) {
                        getComponent(declarationType)
                    } else if (scopeAbility.currentScope() != null && lifecycle == Lifecycle.Scoped) {
                        getComponent(declarationType)
                    }
                }
            }

            /**
             * the function callback after create a scope.
             */
            @Suppress("UNUSED_PARAMETER")
            private fun onCreateScope(scope: Scope) {
                declares.forEach {
                    if (!it.isLazyComponent && it.lifecycle == Lifecycle.Scoped) {
                        getComponent(it.indexTypes[0])
                    }
                }
            }

            /**
             * the function callback after remove a scope.
             */
            private fun onRemoveScope(scope: Scope) {
                declares.forEach {
                    if (it.lifecycle == Lifecycle.Scoped) {
                        storage.remove(
                            it.createKey(scope)
                        )
                    }
                }
            }
        }.apply { onInit() }
    }
}