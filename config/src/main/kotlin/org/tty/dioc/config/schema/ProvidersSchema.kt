package org.tty.dioc.config.schema

import kotlin.reflect.KClass

/**
 * providers schema.
 * the value will be provided by provides. which is order semitic.
 */
@ConfigListable
class ProvidersSchema<T: Any>(
    override val name: String,
    override val type: KClass<T>,
    val default: List<KClass<out T>> = listOf(),
    override val rule: ConfigRule
): ConfigSchema<T> {
    override fun info(): String {
        return "${type.simpleName} <--- ${default.map { it.simpleName }}"
    }

    override val tag: String
        get() = "provider"

    override fun toString(): String {
        return "[$name] $tag (${type.simpleName}) ${info()}"
    }
}