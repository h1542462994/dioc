package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * apis for add a [ComponentDeclare].
 */
interface ServiceDeclareAware {
    /**
     * to add a singleton service
     * @param type the implementation type also the declaration type.
     * @param lazy the service will be created on the boot.
     */
    fun <T: Any> addSingleton(type: KClass<T>, lazy: Boolean = true)

    /**
     * to add a singleton service
     * @param declarationType declaration type.
     * @param implementationType implementation type.
     * @param lazy the service will be created on the boot.
     */
    fun <TD: Any, TI: Any> addSingleton(declarationType: KClass<TD>, implementationType: KClass<TI>, lazy: Boolean = true)


    /**
     * to add a scoped service
     * @param type the implementation type also the declaration type.
     * @param lazy the service will be created on the boot.
     */
    fun <T: Any> addScoped(type: KClass<T>, lazy: Boolean = true)

    /**
     * to add a scoped service
     * @param declarationType declaration type.
     * @param implementationType implementation type.
     * @param lazy the service will be created on the boot.
     */
    fun <TD: Any, TI: Any> addScoped(declarationType: KClass<TD>, implementationType: KClass<TI>, lazy: Boolean = true)

    /**
     * to add a transient service
     * @param type the implementation type also the declaration type.
     */
    fun <T: Any> addTransient(type: KClass<T>)

    /**
     * to add a transient service
     * @param declarationType declaration type.
     * @param implementationType implementation type.
     */
    fun <TD: Any, TI: Any> addTransient(declarationType: KClass<TD>, implementationType: KClass<TI>)

    /**
     * the [action] can replace serviceDeclare by force.
     */
    fun forceReplace(action: (ServiceDeclareAware) -> Unit)
}