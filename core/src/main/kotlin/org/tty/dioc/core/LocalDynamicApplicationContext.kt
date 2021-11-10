package org.tty.dioc.core

import org.tty.dioc.core.declare.ComponentDeclaresImpl
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.basic.ScopeFactory

/**
 * a implementation of [DynamicApplicationContext]
 * @see [DynamicApplicationContext]
 * @see [DefaultDynamicApplicationContext]
 */
@Deprecated("LocalDynamicApplicationContext is replaced with startKernel.")
class LocalDynamicApplicationContext(scopeFactory: ScopeFactory = DefaultScopeFactory())
    : DefaultDynamicApplicationContext(
    ComponentDeclaresImpl(),
    scopeFactory
) {
//    init {
//        super.onInit()
//    }
}