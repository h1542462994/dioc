package org.tty.dioc.config.schema

import kotlin.reflect.KClass

class DataSchema<T: Any> (
    override val name: String,
    override val type: KClass<T>,
    val default: T,
    override val rule: ConfigRule
): ConfigSchema {
    override val tag: String
        get() = "data"

    override fun info(): String {
        return "default = $default"
    }

    override fun toString(): String {
        return "[$name] $tag (${type.simpleName}) ${info()}"
    }
}