package org.tty.dioc.config.schema

import kotlin.reflect.KClass

/**
 * indirect visitor for schema.
 */
@Suppress("unused")
class PathSchema<T: Any>(
    /**
     * path expression, for example (user.)name.length
     */
    val path: String,
    override val type: KClass<T>,
    /**
     * referent [ConfigSchema]
     */
    val referent: ConfigSchema,

    override val rule: ConfigRule
): ConfigSchema {
    init {
        require(referent !is PathSchema<*>) {
            "couldn't use PathSchema as a reference, you could use ConfigSchema pathTo ... to use indirect reference."
        }
    }

    /**
     * the name is extended by [referent], use dot expression.
     */
    override val name: String
        get() = referent.name + "." + path

    override val tag: String
        get() = "path"

    override fun info(): String {
        return "${referent.name} . $path"
    }

    override fun toString(): String {
        return "[$name] $tag (${type.simpleName}) ${info()}"
    }
}
