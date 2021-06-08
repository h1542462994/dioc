package org.tty.dioc.core

import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.ServiceDeclare.Companion.findByDeclare
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.storage.ServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import kotlin.reflect.KClass

/**
 * the default implementation for applicationContext
 * @see [ApplicationContext]
 */
open class DefaultApplicationContext(private val _declarations: List<ServiceDeclare>) : ApplicationContext, InitializeAware {

    override fun <T : Any> getService(type: KClass<T>): T {
        val declare = declarations.findByDeclare(type)
        val creator = ServiceEntry<T>(storage, declarations, declare, this)
        return creator.getOrCreateService()
    }

    override fun currentScope(): Scope? {
        return null
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

    /**
     * the declaration of the services.
     */
    val declarations: List<ServiceDeclare>
    get() = _declarations

    /**
     * the storage of the services.
     */
    protected val storage: ServiceStorage = ServiceStorage()

    override fun onInit() {

    }

}