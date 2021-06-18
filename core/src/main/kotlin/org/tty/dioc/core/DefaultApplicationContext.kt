package org.tty.dioc.core

import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.findByDeclare
import org.tty.dioc.core.lifecycle.DefaultScopeFactory
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.storage.ServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.util.Builder
import kotlin.reflect.KClass

/**
 * the default implementation for applicationContext
 * @see [ApplicationContext]
 */
open class DefaultApplicationContext(private val _declarations: List<ServiceDeclare>) : ApplicationContext, InitializeAware {

    override fun <T : Any> getService(declareType: KClass<T>): T {
        val declare = declarations.findByDeclare(declareType)
        val creator = ServiceEntry<T>(storage, declarations, declare, this)
        return creator.getOrCreateService()
    }

    override fun currentScope(): Scope? {
        return threadLocalScope.get()
    }

    override fun beginScope(): Scope {
        val scope = scopeFactory.create()
        records.get().add(scope)
        threadLocalScope.set(scope)
        return scope
    }

    override fun beginScope(scope: Scope) {
        records.get().add(scope)
        threadLocalScope.set(scope)
    }

    override fun endScope() {
        val scope = threadLocalScope.get() ?: throw IllegalStateException("there are not scope")
        records.get().remove(scope)
        threadLocalScope.set(null)

    }

    override fun endScope(scope: Scope) {
        val exists = records.get().contains(scope)
        if (!exists) {
            throw IllegalStateException("there are not scope like $scope")
        }
        records.get().remove(scope)
    }

    override fun withScope(action: (Scope) -> Unit) {
        val scope = beginScope()
        action(scope)
        endScope()
    }


    /**
     * the declaration of the services.
     */
    val declarations: List<ServiceDeclare>
    get() = _declarations

    private val threadLocalScope: ThreadLocal<Scope?> = ThreadLocal()
    private val records: ThreadLocal<ArrayList<Scope>> = ThreadLocal<ArrayList<Scope>>().also {
        it.set(arrayListOf())
    }
    private val scopeFactory: Builder<Scope> = DefaultScopeFactory()

    /**
     * the storage of the services.
     */
    private val storage: ServiceStorage = ServiceStorage()

    override fun onInit() {
        declarations.forEach {
            if (!it.isLazyService) {
                getService(it.declarationTypes[0])
            }
        }
    }

}