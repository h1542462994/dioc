package org.tty.dioc.core.declare

import kotlin.reflect.KClass

interface ServiceDeclareAware {
    fun <T: Any> addSingleton(type: KClass<T>, lazy: Boolean = true)
    fun <TD: Any, TI: Any> addSingleton(declareType: KClass<TD>, implementationType: KClass<TI>, lazy: Boolean = true)
    fun <T: Any> addScoped(type: KClass<T>)
    fun <TD: Any, TI: Any> addScoped(declareType: KClass<TD>, implementationType: KClass<TI>)
    fun <T: Any> addTransient(type: KClass<T>)
    fun <TD: Any, TI: Any> addTransient(declareType: KClass<TD>, implementationType: KClass<TI>)
    fun <TD: Any, TI: Any> replaceImplementation(declareType: KClass<TD>, implementationType: KClass<TI>, lifecycle: Lifecycle? = null)
}