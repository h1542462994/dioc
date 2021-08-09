package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * apis for add a [ServiceDeclare].
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
    fun <T: Any> addScoped(type: KClass<T>, lazy: Boolean = true)
    fun <TD: Any, TI: Any> addScoped(declarationType: KClass<TD>, implementationType: KClass<TI>, lazy: Boolean = true)
    fun <T: Any> addTransient(type: KClass<T>)
    fun <TD: Any, TI: Any> addTransient(declarationType: KClass<TD>, implementationType: KClass<TI>)
    fun forceReplace(action: (ServiceDeclareAware) -> Unit)
}