package org.tty.dioc.core.launcher

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.ConfigModule
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.ProvidersSchema
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.basic.BasicComponentStorage
import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.core.basic.ProviderResolver
import org.tty.dioc.core.basic.addInternalComponent
import org.tty.dioc.core.internal.BasicComponentStorageImpl
import org.tty.dioc.core.internal.BasicProviderResolver
import org.tty.dioc.core.launcher.BasicComponentKeys.configModule
import org.tty.dioc.core.launcher.BasicComponentKeys.configSchemas
import org.tty.dioc.core.launcher.BasicComponentKeys.providerResolver
import org.tty.dioc.core.launcher.BasicComponentKeys.basicComponentStorage
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.storage.CombinedComponentStorage
import kotlin.reflect.KClass

/**
 * provide the basic
 */
class KernelLoader {
    private val componentStorage = CombinedComponentStorage()

    private inline fun <reified T : Any> addProvider() {
        val configSchemas = componentStorage.configSchemas
        val providerResolver = componentStorage.providerResolver
        val providerSchema: ProvidersSchema<T> = configSchemas.getDefaultProvider(T::class)
        addComponent<T>(providerSchema.name, providerResolver.resolveProvider(providerSchema.name))
    }

    private inline fun <reified T: Any> addComponent(name: String, component: T) {
        componentStorage.addInternalComponent(name, component)
    }

    private fun addBasicComponentStorage() {
        addComponent<ComponentStorage>(basicComponentStorage, componentStorage)
    }

    private fun addConfigSchemas() {
        addComponent(configSchemas, ConfigSchemas())
    }

    private fun moduleInit() {
        addComponent(configModule, ConfigModule(configSchemas = componentStorage.configSchemas))
    }

    private fun addProviderResolver() {
        addComponent<ProviderResolver>(providerResolver, BasicProviderResolver(componentStorage))
    }

    @Suppress("UNCHECKED_CAST")
    fun load(): ApplicationContext {
        // ----------------- create basic suites ----------------------
        // load the configSchemas
        addBasicComponentStorage()
        addConfigSchemas()
        // load all modules, and register the config
        // it must be call there.
        moduleInit()
        // load the providerResolver
        addProviderResolver()
        addProvider<ApplicationConfig>()

        // ------------------ create informal suites -------------------


        return object: ApplicationContext {
            val componentStorage = this@KernelLoader.componentStorage

            override fun <T : Any> getComponent(declareType: KClass<T>): T {
                val component = componentStorage.findComponent(declareType)
                require(component != null) {
                    "component $declareType not found."
                }
                return component
            }

            override fun scopeAbility(): ScopeAbility {
                TODO("Not yet implemented")
            }

            override fun onInit() {
                TODO("Not yet implemented")
            }
        }
    }
}