package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi
import org.tty.dioc.core.lifecycle.ComponentProxyFactoryImpl

/**
 * factory for creating proxy object.
 * @see ComponentProxyFactoryImpl
 */
interface ComponentProxyFactory {
    fun createProxy(): Any
}