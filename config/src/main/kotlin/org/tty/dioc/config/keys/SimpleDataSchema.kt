package org.tty.dioc.config.keys

import kotlin.reflect.KClass

class SimpleDataSchema<T: Any>(
    override val name: String,
    val dataType: KClass<T>,
    val default: T,
    val useFile: Boolean = true
    ) : KeySchema {
}