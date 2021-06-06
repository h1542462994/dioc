package org.tty.dioc.core

import org.tty.dioc.core.declare.ServiceDeclarations

/**
 * to get applicationContext of the package
 */
class LocalApplicationContext(packageName: String) : DefaultApplicationContext(
    ApplicationContextBuilder().usePackage(packageName, true).getDeclarations()
)