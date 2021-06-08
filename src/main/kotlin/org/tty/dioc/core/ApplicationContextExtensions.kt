package org.tty.dioc.core

import kotlin.reflect.KClass

/**
 * to get the service of [T]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Any> ApplicationContext.getService(): T {
    val type: KClass<T> = T::class
    return this.getService(type)
}