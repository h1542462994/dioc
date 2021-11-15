@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package org.tty.dioc.core.basic

import org.tty.dioc.annotation.Lifecycle
import kotlin.reflect.KClass

/**
 * get the component by [T]
 * @param T **index type**, index type can be found by [ComponentAware].
 * @see [ComponentAware.getComponent]
 */
inline fun <reified T : Any> ComponentAware.getComponent(): T {
    val type: KClass<T> = T::class
    return this.getComponent(type)
}

/**
 * get the component by [T]
 * @param name identify of the component.
 * @param T **index type**, index type can be found by [ComponentAware].
 * @see [ComponentAware.getComponent]
 */
inline fun <reified T: Any> ComponentAware.getComponent(name: String): T {
    val type: KClass<T> = T::class
    return this.getComponent(name, type)
}

/**
 * add a [Lifecycle.Singleton] component.
 * @param T **real type** also **index type**, index type can be found by [ComponentAware].
 * @param lazy whether to start on boot. **true** means not to start on boot.
 * @see [ComponentDeclareAware.addSingleton]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ComponentDeclareAware.addSingleton(lazy: Boolean = true) {
    val t: KClass<T> = T::class
    return this.addSingleton(t, lazy)
}

/**
 * add a [Lifecycle.Singleton] component.
 * @param TI **index type**, index type can be found by [ComponentAware].
 * @param TR **real type**.
 * @param lazy whether to start on boot. **true** means not to start on boot.
 * @see [ComponentDeclareAware.addSingleton]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TI: Any, reified TR: Any> ComponentDeclareAware.addSingleton2(lazy: Boolean = true) {
    val ti: KClass<TI> = TI::class
    val tr: KClass<TR> = TR::class
    return this.addSingleton(ti, tr, lazy)
}

/**
 * add a [Lifecycle.Scoped] component.
 * @param T **real type** also **index type**, index type can be found by [ComponentAware].
 * @param lazy whether to start on boot. **true** means not to start on boot.
 * @see [ComponentDeclareAware.addScoped]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ComponentDeclareAware.addScoped(lazy: Boolean = true) {
    val t: KClass<T> = T::class
    return this.addScoped(t, lazy)
}

/**
 * add a [Lifecycle.Scoped] component.
 * @param TI **index type**, index type can be found by [ComponentAware].
 * @param TR **real type**.
 * @param lazy whether to start on boot. **true** means not to start on boot.
 * @see [ComponentDeclareAware.addScoped]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TI: Any, reified TR: Any> ComponentDeclareAware.addScoped2(lazy: Boolean = true) {
    val ti: KClass<TI> = TI::class
    val tr: KClass<TR> = TR::class
    return this.addScoped(ti, tr, lazy)
}

/**
 * add a [Lifecycle.Transient] component.
 * @param T **real type** also **index type**, index type can be found by [ComponentAware].
 * @see [ComponentDeclareAware.addTransient]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T: Any> ComponentDeclareAware.addTransient() {
    val t: KClass<T> = T::class
    return this.addTransient(t)
}

/**
 * add a [Lifecycle.Transient] component.
 * @param TI **index type**, index type can be found by [ComponentAware].
 * @param TR **real type**.
 * @see [ComponentDeclareAware.addTransient]
 */
@OptIn(ExperimentalStdlibApi::class)
inline fun <reified TI: Any, reified TR: Any> ComponentDeclareAware.addTransient2() {
    val ti: KClass<TI> = TI::class
    val tr: KClass<TR> = TR::class
    return this.addTransient(ti, tr)
}

