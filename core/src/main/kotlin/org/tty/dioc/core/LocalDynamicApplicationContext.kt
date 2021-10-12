package org.tty.dioc.core

import org.tty.dioc.core.declare.ComponentDeclares
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
    ComponentDeclares(listOf()),
    scopeFactory
) {
//    init {
//        super.onInit()
//    }
}