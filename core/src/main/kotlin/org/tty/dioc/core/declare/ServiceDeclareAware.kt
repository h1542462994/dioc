package org.tty.dioc.core.declare

import kotlin.reflect.KClass

interface ServiceDeclareAware {
    /**
     * to add a singleton service
     * @param type the implementation type also the declaration type.
     * @param lazy the service will be created on the boot.
     */
    fun <T: Any> addSingleton(type: KClass<T>, lazy: Boolean = true)
    fun <TD: Any, TI: Any> addSingleton(declarationType: KClass<TD>, implementationType: KClass<TI>, lazy: Boolean = true)
    fun <T: Any> addScoped(type: KClass<T>, lazy: Boolean = true)
    fun <TD: Any, TI: Any> addScoped(declarationType: KClass<TD>, implementationType: KClass<TI>, lazy: Boolean = true)
    fun <T: Any> addTransient(type: KClass<T>)
    fun <TD: Any, TI: Any> addTransient(declarationType: KClass<TD>, implementationType: KClass<TI>)
    fun <TD: Any, TI: Any> replaceImplementation(declarationType: KClass<TD>, implementationType: KClass<TI>, lifecycle: Lifecycle? = null)
}