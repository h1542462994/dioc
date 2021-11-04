package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.NoInfer
import kotlin.reflect.KClass

@Deprecated("use CombinedComponentStorage instead.")
@InternalComponent
interface BasicComponentStorage {
    fun <T : Any> addComponent(name: String, component: T)
    fun <T : Any> addComponent(name: String, interfaceType: KClass<out T>, component: T)
    fun <@NoInfer T : Any> getComponent(name: String): T
    fun <T : Any> getComponent(interfaceType: KClass<T>): T
}