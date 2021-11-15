package org.tty.dioc.core.launcher

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.ConfigModule
import org.tty.dioc.config.schema.ConfigSchema
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.ProvidersSchema
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.ApplicationEntryPoint
import org.tty.dioc.core.CoreModule
import org.tty.dioc.core.basic.*
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.internal.CombinedProviderResolver
import org.tty.dioc.core.internal.CombinedComponentStorage
import org.tty.dioc.util.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

/**
 * provide the basic
 */
class KernelLoader {
    /**
     * use [CombinedComponentStorage] as the [ComponentStorage]
     */
    private val componentStorage: ComponentStorage = CombinedComponentStorage()

    private val providerResolver: ProviderResolver = CombinedProviderResolver(componentStorage)

    /**
     * entry point to the [ApplicationContext]
     */
    private lateinit var entryPoint: ApplicationEntryPoint

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
     * set [ApplicationEntryPoint]
     */
    fun setApplicationEntryPoint(applicationEntryPoint: ApplicationEntryPoint): KernelLoader {
        this.entryPoint = applicationEntryPoint
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

        addProvider<EntryPointLoader>()

        // add root components
        // call on startup
        entryPoint.onStartUp(
            componentDeclares
        )


        return object: ApplicationContext {
            val componentStorage = this@KernelLoader.componentStorage

            override fun <T : Any> getComponent(indexType: KClass<T>): T {
                return if (indexType.hasAnnotation<InternalComponent>()) {
                    // TODO("当前通过ComponentResolver去创建InternalComponent.")
                    requireNotNull(componentStorage.findComponent(indexType)) {
                        "internalComponent $indexType is not existed."
                    }
                } else {
                    componentStorage
                        .getInternalComponent(CoreModule.componentResolverSchema)
                        .resolve(declare = componentDeclares.singleIndexType(indexType = indexType))
                }
            }

            override fun <T : Any> getComponent(name: String, indexType: KClass<T>): T {
                TODO("Not yet implemented")
            }

            override fun scopeAbility(): ScopeAbility {
                return componentStorage.getInternalComponent(CoreModule.scopeAbilitySchema)
            }

            override fun onInit() {

            }
        }.apply { onInit() }
    }
}