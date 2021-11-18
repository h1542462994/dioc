package org.tty.dioc.config.schema

import kotlin.reflect.KClass
import org.tty.dioc.config.ApplicationConfig

/**
 * [ConfigSchema] for data. the data can be used by [ApplicationConfig] and [PathSchema].
 */
class DataSchema<T: Any> (
    override val name: String,
    override val type: KClass<T>,
    val default: T,
    override val rule: ConfigRule
): ConfigSchema<T> {
    override val tag: String
        get() = "data"

    override fun info(): String {
        return "default = $default"
    }

    override fun toString(): String {
        return "[$name] $tag (${type.simpleName}) ${info()}"
    }
}