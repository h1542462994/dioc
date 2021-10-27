package org.tty.dioc.config.schema

import kotlin.reflect.KClass

class DataSchema<T: Any> (
    override val name: String,
    val dataType: KClass<T>,
    val default: T,
    override val rule: ConfigRule = ConfigRule.Declare
): ConfigSchema