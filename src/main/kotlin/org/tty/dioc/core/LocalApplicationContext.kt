package org.tty.dioc.core

/**
 * to get applicationContext of the package
 */
class LocalApplicationContext(packageName: String) : DefaultApplicationContext(
    ApplicationContextBuilder().usePackage(packageName, true).getDeclarations()
)