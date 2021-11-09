package org.tty.dioc.config.schema

import kotlin.reflect.KClass

/**
 * [ConfigSchema] for data. the data can be used by configuration and path visitation.
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