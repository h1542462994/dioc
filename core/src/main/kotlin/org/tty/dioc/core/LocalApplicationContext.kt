package org.tty.dioc.core

import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.core.declare.ServiceDeclareResolver

/**
 * to get applicationContext of the package
 */
class LocalApplicationContext(packageName: String) : DefaultApplicationContext(
    ServiceDeclareResolver(scanPackages = arrayListOf(PackageOption(packageName, inclusive = true))).getDeclarations()
) {
    init {
        super.onInit()
    }
}