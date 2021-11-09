package org.tty.dioc.config.internal

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.*
import org.tty.dioc.error.notProvided
import org.tty.dioc.error.unSupported
import org.tty.dioc.reflect.getWithPropertyChain
import kotlin.reflect.full.hasAnnotation

/**
 * basic [ApplicationConfig]
 * only get the config from [ConfigSchemas]
 */
@InternalComponent
class ApplicationConfigDeclareSupport : ApplicationConfig {
    override fun <T : Any> get(configSchema: ConfigSchema<T>): Any {
        return when (configSchema) {
            is DataSchema<*> -> {
                configSchema.default
            }
            is ProvidersSchema<*> -> {
                if (configSchema.default.isEmpty()) {
                    notProvided("empty provider.")
                } else if (configSchema.default.size > 1) {
                    notProvided("has more than 1 provider.")
                }
                configSchema.default.first()
            }
            is PathSchema<*> -> {
                val chain = configSchema.name.substring(configSchema.referent.name.length + 1)
                get(configSchema.referent).getWithPropertyChain(chain)!!
            }
            else -> unSupported("unreachable code.")
        }
    }

    override fun <T : Any> set(configSchema: ConfigSchema<T>, value: Any) {
        notProvided("declare support has not provided.")
    }

    override fun <T : Any> getList(configSchema: ConfigSchema<T>): List<*> {
        require(configSchema::class.hasAnnotation<ConfigListable>()) {
            "only configListable can getList"
        }
        return when (configSchema) {
            is ProvidersSchema<T> -> {
                configSchema.default
            }
            else -> unSupported("unreachable code.")
        }
    }

    override fun <T : Any> setList(configSchema: ConfigSchema<T>, list: List<*>) {
        notProvided("declare support has not provided.")
    }




}