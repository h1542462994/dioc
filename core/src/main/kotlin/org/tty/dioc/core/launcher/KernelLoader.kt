package org.tty.dioc.core.launcher

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.ConfigModule
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.ProvidersSchema
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.internal.BasicComponentStorageImpl
import org.tty.dioc.core.internal.BasicProviderResolver
import org.tty.dioc.core.launcher.BasicComponentKeys.configModule
import org.tty.dioc.core.launcher.BasicComponentKeys.configSchemas
import org.tty.dioc.core.launcher.BasicComponentKeys.providerResolver
import org.tty.dioc.core.lifecycle.ScopeAbility
import kotlin.reflect.KClass

/**
 * provide the basic
 */
class KernelLoader {
    private val componentStorage = BasicComponentStorageImpl()

    private inline fun <reified T : Any> addProvider() {
        val configSchemas = componentStorage.configSchemas
        val providerResolver = componentStorage.providerResolver
        val providerSchema: ProvidersSchema<T> = configSchemas.getProvider(T::class)
        componentStorage.addComponent(providerSchema.name, providerResolver.resolveProvider(providerSchema.name))
    }

    private fun addConfigSchemas() {
        componentStorage.addComponent(configSchemas, ConfigSchemas())
    }

    private fun moduleInit() {
        componentStorage.addComponent(configModule, ConfigModule(configSchemas = componentStorage.configSchemas))
    }

    private fun addProviderResolver() {
        componentStorage.addComponent(providerResolver, BasicProviderResolver(componentStorage))
    }

    fun load(): ApplicationContext {
        // load the configSchemas
        addConfigSchemas()
        // load all modules, and register the config
        // it must be call there.
        moduleInit()
        // load the providerResolver
        addProviderResolver()
        addProvider<ApplicationConfig>()

        return object: ApplicationContext {
            val componentStorage = this@KernelLoader.componentStorage

            override fun <T : Any> getComponent(declareType: KClass<T>): T {
                TODO("Not yet implemented")
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