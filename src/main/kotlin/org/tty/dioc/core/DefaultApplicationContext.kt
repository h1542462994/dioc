package org.tty.dioc.core

import org.tty.dioc.core.declare.ServiceDeclarations
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.storage.ServiceStorage
import org.tty.dioc.core.util.ServiceEntry

/**
 * the default implementation for applicationContext
 * @see [ApplicationContext]
 */
open class DefaultApplicationContext(private val _declarations: ServiceDeclarations) : ApplicationContext, InitializeAware {

    override fun <T> getService(type: Class<T>): T {
        val declare = declarations.findByDeclare(type)!!
        val creator = ServiceEntry<T>(storage, declarations, declare, null)
        return creator.getOrCreateService()
    }

    override fun <T> getService(type: Class<T>, scope: Scope): T {
        TODO("Not yet implemented")
    }

    override fun currentScope(): Scope? {
        TODO("Not yet implemented")
    }

    override fun beginScope(): Scope {
        TODO("Not yet implemented")
    }

    override fun endScope() {
        TODO("Not yet implemented")
    }

    override fun endScope(scope: Scope) {
        TODO("Not yet implemented")
    }

    override fun withScope(action: (Scope) -> Unit) {
        TODO("Not yet implemented")
    }

    private fun findDeclare(type: Class<*>): ServiceDeclare {
        return declarations.single { it.serviceElement.declarationTypes.contains(type) }
    }

    /**
     * the declaration of the services.
     */
    val declarations: ServiceDeclarations
    get() = _declarations

    /**
     * the storage of the services.
     */
    protected val storage: ServiceStorage = ServiceStorage()

    override fun onInit() {

    }

}