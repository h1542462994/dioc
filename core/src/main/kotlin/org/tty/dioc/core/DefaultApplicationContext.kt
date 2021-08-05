package org.tty.dioc.core

import org.tty.dioc.core.declare.ReadonlyServiceDeclares
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.ServiceDeclares
import org.tty.dioc.core.lifecycle.*
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.util.Builder
import kotlin.reflect.KClass

/**
 * the default implementation for applicationContext
 * @see [ApplicationContext]
 */
open class DefaultApplicationContext(
    private val _declarations: List<ServiceDeclare>,
    scopeFactory: Builder<Scope>
    ) : ApplicationContext, InitializeAware {

    /**
     * root function to get the service by [declareType]
     */
    override fun <T : Any> getService(declareType: KClass<T>): T {
        val declare = declarations.findByDeclare(declareType)
        return entry.getOrCreateService(declare)
    }

    /**
     * the ability of the [Scope]
     */
    override fun scopeAbility(): ScopeAbility {
        return scopeTrace
    }

    /**
     * the declaration of the services.
     */
    private lateinit var declarations: ReadonlyServiceDeclares

    /**
     * the entry to get the service.
     */
    private lateinit var entry: ServiceEntry

    private val scopeTrace: ScopeAbility = ScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedServiceStorage()

    override fun onInit() {
        declarations = ServiceDeclares(_declarations)
        entry = ServiceEntry(declarations, storage, scopeTrace)
        declarations.forEach {
            if (!it.isLazyService) {
                getService(it.declarationTypes[0])
            }
        }
    }

}