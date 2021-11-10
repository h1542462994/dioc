package org.tty.dioc.core.launcher

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.ConfigModule
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.ProvidersSchema
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.ApplicationEntryPoint
import org.tty.dioc.core.CoreModule
import org.tty.dioc.core.basic.*
import org.tty.dioc.core.declare.*
import org.tty.dioc.core.internal.BasicProviderResolver
import org.tty.dioc.core.internal.CombinedProviderResolver
import org.tty.dioc.core.internal.ComponentDeclareResolver
import org.tty.dioc.core.key.SingletonKey
import org.tty.dioc.core.launcher.ComponentKeys.configModule
import org.tty.dioc.core.launcher.ComponentKeys.configSchemas
import org.tty.dioc.core.launcher.ComponentKeys.coreModule
import org.tty.dioc.core.launcher.ComponentKeys.providerResolver
import org.tty.dioc.core.storage.CombinedComponentStorage
import org.tty.dioc.util.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

/**
 * provide the basic
 */
class KernelLoader {
    private val componentStorage = CombinedComponentStorage()
    private lateinit var entryPoint: ApplicationEntryPoint

    private inline fun <reified T : Any> addProvider() {
        val configSchemas = componentStorage.configSchemas
        val providerResolver = componentStorage.providerResolver
        val providerSchema: ProvidersSchema<T> = configSchemas.getDefaultProvider(T::class)
        addComponent<T>(providerSchema.name, providerResolver.resolveProvider(providerSchema.name))
    }

    private inline fun <reified T: Any> addComponent(name: String, component: T) {
        componentStorage.addInternalComponent(name, component)
    }

    private fun addComponentStorage() {
        addComponent<ComponentStorage>(ComponentKeys.componentStorage, componentStorage)
    }

    private fun addConfigSchemas() {
        addComponent(configSchemas, ConfigSchemas())
    }

    private fun moduleInit() {
        addComponent(configModule, ConfigModule(configSchemas = componentStorage.configSchemas))
        addComponent(coreModule, CoreModule(configSchemas = componentStorage.configSchemas))
    }

    private fun addBasicProviderResolver() {
        addComponent<ProviderResolver>(providerResolver, BasicProviderResolver(componentStorage))
    }

    private fun replaceCombinedProviderResolver(){
        componentStorage.remove(SingletonKey(ProviderResolver::class, providerResolver))
        addComponent<ProviderResolver>(providerResolver, CombinedProviderResolver(componentStorage))
    }



    fun setApplicationEntryPoint(applicationEntryPoint: ApplicationEntryPoint): KernelLoader {
        this.entryPoint = applicationEntryPoint
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun load(): ApplicationContext {
        // ----------------- create basic suites ----------------------

        addComponentStorage()
        // load the configSchemas
        addConfigSchemas()
        // load all modules, and register the config
        // it must be call there.
        moduleInit()
        // load the providerResolver
        addBasicProviderResolver()
        addProvider<ApplicationConfig>()

        // ------------------ create informal suites -------------------

        replaceCombinedProviderResolver()
        addProvider<Logger>()
        addProvider<ScopeFactory>()
        addProvider<ScopeAbility>()
        addProvider<ReadonlyComponentDeclares>()
        // call on configuration
        entryPoint.onConfiguration(componentStorage.getInternalComponent(ConfigModule.configSchema))
        val componentDeclares = componentStorage.getInternalComponent(CoreModule.componentDeclaresSchema)
                as MutableComponentDeclares

        // add root components
        val rootPackName = entryPoint.javaClass.packageName
        componentDeclares.addAll(
            ComponentDeclareResolver(
                arrayListOf(), arrayListOf(
                    PackageOption(name = rootPackName, true)
                )
            ).getDeclarations()
        )

        // call on startup
        entryPoint.onStartUp(
            componentDeclares
        )
        addProvider<ComponentResolver>()

        return object: ApplicationContext {
            val componentStorage = this@KernelLoader.componentStorage

            override fun <T : Any> getComponent(declareType: KClass<T>): T {
                return if (declareType.hasAnnotation<InternalComponent>()) {
                    // TODO("当前通过ComponentResolver去创建InternalComponent.")
                    requireNotNull(componentStorage.findComponent(declareType)) {
                        "internalComponent $declareType is not existed."
                    }
                } else {
                    componentStorage
                        .getInternalComponent(CoreModule.componentResolverSchema)
                        .resolve(declare = componentDeclares.singleDeclarationType(declarationType = declareType))
                }
            }

            override fun scopeAbility(): ScopeAbility {
                return componentStorage.getInternalComponent(CoreModule.scopeAbilitySchema)
            }

            override fun onInit() {

            }
        }.apply { onInit() }
    }
}