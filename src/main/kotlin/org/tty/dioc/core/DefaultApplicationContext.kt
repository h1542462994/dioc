package org.tty.dioc.core

import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.storage.ServiceDeclarations

/**
 * the default implementation for applicationContext
 * @see [ApplicationContext]
 */
class DefaultApplicationContext(private val _declarations: ServiceDeclarations) : ApplicationContext, InitializeAware {

    override fun <T> getService(): T {
        TODO("Not yet implemented")
    }

    val declarations: ServiceDeclarations
    get() = _declarations

    override fun onInit() {

    }

}