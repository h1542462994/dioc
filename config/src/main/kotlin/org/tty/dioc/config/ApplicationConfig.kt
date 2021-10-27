package org.tty.dioc.config

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigSchema
import kotlin.reflect.KClass

@InternalComponent
interface ApplicationConfig {
    operator fun <T: Any> get(name: String): T
    operator fun <T: Any> set(name: String, value: T)
    operator fun <T: Any> get(configSchema: ConfigSchema): T
    operator fun <T: Any> set(configSchema: ConfigSchema, value: T)
    operator fun <T: Any> get(type: KClass<T>): T
}

