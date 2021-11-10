package org.tty.dioc.config.schema

import kotlin.reflect.KClass

/**
 * schema of the key.
 * the declaration is registered in [ConfigSchemas]
 * @see ConfigSchemas
 */
interface ConfigSchema<T: Any> {
    /**
     * the name of the schema
     */
    val name: String

    /**
     * the present type
     */
    val type: KClass<T>

    /**
     * tag value
     */
    val tag: String
    /**
     * the rule of the schema
     */
    val rule: ConfigRule

    fun info(): String
}