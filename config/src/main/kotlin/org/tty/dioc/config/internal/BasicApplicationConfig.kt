package org.tty.dioc.config.internal

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.*
import org.tty.dioc.error.notProvided
import org.tty.dioc.error.unSupported
import org.tty.dioc.reflect.getWithPropertyChain
import kotlin.reflect.KClass

/**
 * basic [ApplicationConfig]
 * only get the config from [ConfigSchemas]
 */
@InternalComponent
class BasicApplicationConfig(
    private val configSchemas: ConfigSchemas
): ApplicationConfig {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(name: String): T {
        val configSchema: ConfigSchema = configSchemas[name] ?: notProvided("couldn't found schema with name $name")
        return getFromSchema(configSchema)
    }

    override fun <T : Any> get(configSchema: ConfigSchema): T
        = get(configSchema.name)

    /**
     * get the config by type, it's not provided in [BasicApplicationConfig]
     */
    override fun <T : Any> get(type: KClass<T>): T = notProvided()

    /**
     * readonly provider, so set is not provided.
     */
    override fun <T : Any> set(name: String, value: T) = notProvided()

    /**
     * readonly provider, so set is not provided.
     */
    override fun <T : Any> set(configSchema: ConfigSchema, value: T) = notProvided()

    /**
     * get the value from the schema
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getFromSchema(configSchema: ConfigSchema): T {
        return when (configSchema) {
            is DataSchema<*> -> {
                configSchema.default as T
            }
            is ProvidersSchema<*> -> {
                configSchema.default as T
            }
            is PathSchema<*> -> {
                val chain = configSchema.name.substring(configSchema.referent.name.length + 1)
                (get(configSchema.referent) as Any).getWithPropertyChain(chain) as T
            }
            else -> unSupported("unreachable code.")
        }
    }

}