package org.tty.dioc.core

import kotlin.reflect.KClass

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Any> ApplicationContext.getService(): T {
    val type: KClass<T> = T::class
    return this.getService(type)
}