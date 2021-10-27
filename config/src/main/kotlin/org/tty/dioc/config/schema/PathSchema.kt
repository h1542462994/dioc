package org.tty.dioc.config.schema

/**
 * indirect visitor for schema.
 */
@Suppress("unused")
class PathSchema<T: Any>(
    override val name: String,
    val ref: String,
    override val rule: ConfigRule = ConfigRule.Declare
): ConfigSchema
