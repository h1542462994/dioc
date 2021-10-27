package org.tty.dioc.config.schema

import kotlin.reflect.KClass

/**
 * a provider schema, binds the schema between
 */
class ProvidersSchema<T: Any>(
    override val name: String,
    override val type: KClass<T>,
    val default: List<KClass<out T>> = listOf(),
    override val rule: ConfigRule
): ConfigSchema {
    override fun info(): String {
        return "${type.simpleName} <--- ${default.map { it.simpleName }}"
    }

    override val tag: String
        get() = "provider"

    override fun toString(): String {
        return "[$name] $tag (${type.simpleName}) ${info()}"
    }
}