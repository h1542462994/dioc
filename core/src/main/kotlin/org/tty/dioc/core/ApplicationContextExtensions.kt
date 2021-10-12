@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package org.tty.dioc.core

import org.tty.dioc.core.basic.ComponentAware
import org.tty.dioc.core.declare.ComponentDeclareAware
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
inline fun <reified T: Any> ComponentDeclareAware.addSingleton(lazy: Boolean = true) {
    val t: KClass<T> = T::class
    return this.addSingleton(t, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TD: Any, reified TI: Any> ComponentDeclareAware.addSingleton2(lazy: Boolean = true) {
    val td: KClass<TD> = TD::class
    val ti: KClass<TI> = TI::class
    return this.addSingleton(td, ti, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ComponentDeclareAware.addScoped(lazy: Boolean = true) {
    val t: KClass<T> = T::class
    return this.addScoped(t, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TD: Any, reified TI: Any> ComponentDeclareAware.addScoped2(lazy: Boolean = true) {
    val td: KClass<TD> = TD::class
    val ti: KClass<TI> = TI::class
    return this.addScoped(td, ti, lazy)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ComponentDeclareAware.addTransient() {
    val t: KClass<T> = T::class
    return this.addTransient(t)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TD: Any, reified TI: Any> ComponentDeclareAware.addTransient2() {
    val td: KClass<TD> = TD::class
    val ti: KClass<TI> = TI::class
    return this.addTransient(td, ti)
}

