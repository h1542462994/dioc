@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package org.tty.dioc.core

import org.tty.dioc.core.declare.ComponentAware
import org.tty.dioc.core.declare.ServiceDeclareAware
import kotlin.reflect.KClass

/**
 * to get the service of [T]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Any> ComponentAware.getService(): T {
    val type: KClass<T> = T::class
    return this.getComponent(type)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ServiceDeclareAware.addSingleton(lazy: Boolean = true) {
    val t: KClass<T> = T::class
    return this.addSingleton(t, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TD: Any, reified TI: Any> ServiceDeclareAware.addSingleton2(lazy: Boolean = true) {
    val td: KClass<TD> = TD::class
    val ti: KClass<TI> = TI::class
    return this.addSingleton(td, ti, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ServiceDeclareAware.addScoped(lazy: Boolean = true) {
    val t: KClass<T> = T::class
    return this.addScoped(t, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TD: Any, reified TI: Any> ServiceDeclareAware.addScoped2(lazy: Boolean = true) {
    val td: KClass<TD> = TD::class
    val ti: KClass<TI> = TI::class
    return this.addScoped(td, ti, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ServiceDeclareAware.addTransient() {
    val t: KClass<T> = T::class
    return this.addTransient(t)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TD: Any, reified TI: Any> ServiceDeclareAware.addTransient2() {
    val td: KClass<TD> = TD::class
    val ti: KClass<TI> = TI::class
    return this.addTransient(td, ti)
}

