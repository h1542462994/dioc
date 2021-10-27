package org.tty.dioc.config.schema

/**
 * schema of the key.
 * the declaration is registered in [ConfigSchemas]
 * @see ConfigSchemas
 */
interface ConfigSchema {
    /**
     * the name of the schema
     */
    val name: String

    /**
     * the rule of the schema
     */
    val rule: ConfigRule
}