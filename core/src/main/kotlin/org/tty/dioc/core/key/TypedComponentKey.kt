package org.tty.dioc.core.key

import kotlin.reflect.KClass

interface TypedComponentKey: ComponentKey {
    val indexType: KClass<*>
}