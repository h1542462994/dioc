package org.tty.dioc.core

import org.tty.dioc.core.declare.ComponentDeclares
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.base.Builder
import org.tty.dioc.core.basic.ScopeFactory

/**
 * a implementation of [DynamicApplicationContext]
 * @see [DynamicApplicationContext]
 * @see [DefaultDynamicApplicationContext]
 */
class LocalDynamicApplicationContext(scopeFactory: ScopeFactory = DefaultScopeFactory())
    : DefaultDynamicApplicationContext(
    ComponentDeclares(),
    scopeFactory
) {
//    init {
//        super.onInit()
//    }
}