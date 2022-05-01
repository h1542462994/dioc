package org.tty.dioc.core.basic

import kotlin.reflect.KClass
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.base.Builder

/**
 * aware for adding [ComponentDeclare]
 */
interface ComponentDeclareAware {

    /**
     * add a [Lifecycle.Singleton] component. **inline available.**
     * @param type **real type** also **index type**, index type can be found by [ComponentAware].
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <T: Any> addSingleton(type: KClass<T>, lazy: Boolean = true)

    /**
     * add a [Lifecycle.Singleton] component. **inline available.**
     * @param name identify of the component.
     * @param type **real type** also **index type**, index type can be found by [ComponentAware].
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <T: Any> addSingleton(name: String, type: KClass<T>, lazy: Boolean = true)

    /**
     * add a [Lifecycle.Singleton] component. **inline available**.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param realType **real type**.
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <TD: Any, TI: Any> addSingleton(indexType: KClass<TD>, realType: KClass<TI>, lazy: Boolean = true)

    /**
     * add a [Lifecycle.Singleton] component. **inline available**.
     * @param name identify of the component.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param realType **real type**.
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <TD: Any, TI: Any> addSingleton(name: String, indexType: KClass<TD>, realType: KClass<TI>, lazy: Boolean = true)


    /**
     * add a [Lifecycle.Singleton] component. **inline available**.
     * @param name identify of the component.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param provided the provided value.
     */
    fun <TD: Any> addSingletonProvided(name: String, indexType: KClass<TD>, provided: TD)

    /**
     * add a [Lifecycle.Singleton] component. **inline available**.
     * @param name identify of the component.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param providedBuilder the provided builder for the component.
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <TD: Any> addSingletonProvided(name: String, indexType: KClass<TD>, providedBuilder: Builder<TD>, lazy: Boolean = true)

    /**
     * add a [Lifecycle.Scoped] component. **inline available.**
     * @param type **real type** also **index type**, index type can be found by [ComponentAware].
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <T: Any> addScoped(type: KClass<T>, lazy: Boolean = true)

    /**
     * add a [Lifecycle.Scoped] component. **inline available.**
     * @param name identify of the component.
     * @param type **real type** also **index type**, index type can be found by [ComponentAware].
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <T: Any> addScoped(name: String, type: KClass<T>, lazy: Boolean = true)

    /**
     * add a [Lifecycle.Scoped] component. **inline available**.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param realType **real type**.
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <TD: Any, TI: Any> addScoped(indexType: KClass<TD>, realType: KClass<TI>, lazy: Boolean = true)

    /**
     * add a [Lifecycle.Scoped] component. **inline available**.
     * @param name identify of the component.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param realType **real type**.
     * @param lazy whether to start on boot. **true** means not to start on boot.
     */
    fun <TD: Any, TI: Any> addScoped(name: String, indexType: KClass<TD>, realType: KClass<TI>, lazy: Boolean = true)


    /**
     * add a [Lifecycle.Transient] component. **inline available.**
     * @param type **real type** also **index type**, index type can be found by [ComponentAware].
     */
    fun <T: Any> addTransient(type: KClass<T>)

    /**
     * add a [Lifecycle.Transient] component. **inline available.**
     * @param type **real type** also **index type**, index type can be found by [ComponentAware].
     * @param name identify of the component.
     */
    fun <T: Any> addTransient(name: String, type: KClass<T>)

    /**
     * add a [Lifecycle.Transient] component. **inline available**.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param realType **real type**.
     */
    fun <TD: Any, TI: Any> addTransient(indexType: KClass<TD>, realType: KClass<TI>)

    /**
     * add a [Lifecycle.Transient] component. **inline available**.
     * @param name identify of the component.
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @param realType **real type**.
     */
    fun <TD: Any, TI: Any> addTransient(name: String, indexType: KClass<TD>, realType: KClass<TI>)

    /**
     * the [action] can replace [ComponentDeclare] by force.
     */
    fun forceReplace(action: ComponentDeclareAware.() -> Unit)
}