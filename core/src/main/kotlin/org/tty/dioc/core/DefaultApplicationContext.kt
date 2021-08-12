package org.tty.dioc.core

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ReadonlyServiceDeclares
import org.tty.dioc.core.lifecycle.*
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.base.Builder
import kotlin.reflect.KClass

/**
 * the default implementation for applicationContext
 * @see [ApplicationContext]
 */
open class DefaultApplicationContext(
    /**
     * the declaration of the services.
     */
    private val declarations: ReadonlyServiceDeclares,
    scopeFactory: Builder<Scope>
    ) : ApplicationContext, InitializeAware {

    /**
     * root function to get the service by [declareType]
     */
    override fun <T : Any> getService(declareType: KClass<T>): T {
        val serviceDeclare = declarations.singleDeclarationType(declareType)
        return entry.getOrCreateService(serviceDeclare)
    }

    /**
     * the ability of the [Scope]
     */
    override fun scopeAbility(): ScopeAbility {
        return scopeTrace
    }

    override fun onInit() {
        entry = ServiceEntry(declarations, storage, scopeTrace)
        declarations.forEach {
            if (!it.isLazyService && it.lifecycle == Lifecycle.Singleton) {
                getService(it.declarationTypes[0])
            }
        }
    }



    /**
     * the entry to get the service.
     */
    private lateinit var entry: ServiceEntry

    /**
     * the trace of the scope.
     */
    private val scopeTrace: ScopeAbility = ScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedServiceStorage()



}