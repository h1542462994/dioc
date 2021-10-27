package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.core.lifecycle.ComponentProxyFactoryImpl

/**
 * factory for creating proxy object.
 * @see ComponentProxyFactoryImpl
 */
@InternalComponent
interface ComponentProxyFactory {
    fun createProxy(): Any
}