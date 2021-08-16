package org.tty.dioc.core

import org.tty.dioc.core.declare.ServiceDeclares
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.base.Builder

/**
 * a implementation of [DynamicApplicationContext]
 * @see [DynamicApplicationContext]
 * @see [DefaultDynamicApplicationContext]
 */
class LocalDynamicApplicationContext(scopeFactory: Builder<Scope> = DefaultScopeFactory())
    : DefaultDynamicApplicationContext(
    ServiceDeclares(listOf()),
    scopeFactory
) {
//    init {
//        super.onInit()
//    }
}