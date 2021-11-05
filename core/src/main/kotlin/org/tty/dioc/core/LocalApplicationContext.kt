package org.tty.dioc.core

import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.internal.ComponentDeclareResolver
import org.tty.dioc.core.declare.ComponentDeclares
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.base.Builder
import org.tty.dioc.core.basic.ScopeFactory

/**
 * a implementation of [ApplicationContext]
 * @see ApplicationContext
 * @see DefaultApplicationContext
 */
class LocalApplicationContext(packageName: String, scopeFactory: ScopeFactory = DefaultScopeFactory())
    : DefaultApplicationContext(
    ComponentDeclares().apply {
        ComponentDeclareResolver(scanPackages = arrayListOf(PackageOption(packageName, inclusive = true))).getDeclarations()
    },
    scopeFactory
) {
    init {
        super.onInit()
    }
}