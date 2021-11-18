package org.tty.dioc.core.basic

import org.tty.dioc.annotation.Lazy
import org.tty.dioc.core.internal.ComponentProxyFactoryImpl

/**
 * factory for creating proxy object.
 * @see ComponentProxyFactoryImpl
 */
interface ComponentProxyFactory {
    /**
     * create the proxy for service inject with [Lazy]
     */
    fun createProxy(): Any
}