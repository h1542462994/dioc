package org.tty.dioc.core

import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.declare.ServiceDeclareResolver
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.util.Builder

/**
 * to get applicationContext of the package
 */
class LocalApplicationContext(packageName: String, scopeFactory: Builder<Scope> = DefaultScopeFactory()) : DefaultApplicationContext(
    ServiceDeclareResolver(scanPackages = arrayListOf(PackageOption(packageName, inclusive = true))).getDeclarations(),
    scopeFactory
) {
    init {
        super.onInit()
    }
}