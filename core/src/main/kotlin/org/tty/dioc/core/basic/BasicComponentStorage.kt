package org.tty.dioc.core.basic

import kotlin.reflect.KClass

interface BasicComponentStorage {
    fun <T : Any> addComponent(name: String, component: T)
    fun <T : Any> addComponent(name: String, interfaceType: KClass<out T>, component: T)
    fun <T : Any> getComponent(name: String): T
    fun <T : Any> getComponent(interfaceType: KClass<T>): T
}