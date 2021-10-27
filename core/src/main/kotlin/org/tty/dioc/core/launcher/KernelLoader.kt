package org.tty.dioc.core.launcher

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.core.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * provide the basic
 */
class KernelLoader {
    private val components = HashMap<KClass<*>, Any>()

    private val configSchema: ConfigSchemas get() {
        return components[ConfigSchemas::class] as ConfigSchemas
    }

    private inline fun <reified T: Any> addComponent() {
        val type = T::class
        if (type == ConfigSchemas::class

        ) {
            components[type] = type.createInstance()
            return
        }


    }

    fun load(): ApplicationContext {
        addComponent<ConfigSchemas>()
        addComponent<ApplicationConfig>()
        TODO()
    }
}