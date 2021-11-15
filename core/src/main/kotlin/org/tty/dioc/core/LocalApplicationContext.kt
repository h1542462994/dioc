package org.tty.dioc.core

import org.tty.dioc.core.basic.ScopeFactory
import org.tty.dioc.core.declare.ComponentDeclaresImpl
import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.internal.ComponentDeclareResolver
import org.tty.dioc.core.lifecycle.DefaultScopeFactory

/**
 * implementation of [ApplicationContext]
 * @see ApplicationContext
 * @see DefaultApplicationContext
 */
@Deprecated("LocalApplicationContext is replaced with startKernel.")
class LocalApplicationContext(packageName: String, scopeFactory: ScopeFactory = DefaultScopeFactory())
    : DefaultApplicationContext(
    ComponentDeclaresImpl().apply {
        ComponentDeclareResolver(scanPackages = arrayListOf(PackageOption(packageName, inclusive = true))).getDeclarations()
    },
    scopeFactory
) {
    init {
        super.onInit()
    }
}