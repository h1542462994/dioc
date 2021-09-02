package org.tty.dioc.config.keys

import kotlin.reflect.KClass


class ProviderKeySchema(
    override val name: String,
    val declareType: KClass<*>,
    val defaultImplementationType: KClass<*>,
    val mutable: Boolean,
): KeySchema {

    override fun toString(): String {
        return "[${name}] (${if (mutable) "mutable" else "immutable"}) ${declareType.simpleName} <--- ${defaultImplementationType.simpleName}"
    }
}