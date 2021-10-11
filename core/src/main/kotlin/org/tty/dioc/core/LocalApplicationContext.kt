package org.tty.dioc.core

import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.internal.ComponentDeclareResolver
import org.tty.dioc.core.declare.ServiceDeclares
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.base.Builder

/**
 * a implementation of [ApplicationContext]
 * @see ApplicationContext
 * @see DefaultApplicationContext
 */
class LocalApplicationContext(packageName: String, scopeFactory: Builder<Scope> = DefaultScopeFactory())
    : DefaultApplicationContext(
    ServiceDeclares(
        ComponentDeclareResolver(scanPackages = arrayListOf(PackageOption(packageName, inclusive = true))).getDeclarations()
    ),
    scopeFactory
) {
    init {
        super.onInit()
    }
}