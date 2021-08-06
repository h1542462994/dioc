package org.tty.dioc.core

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.lifecycle.ScopeTrace
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.util.Builder
import kotlin.reflect.KClass

class DefaultDynamicApplicationContext(
    private val _declarations: List<ServiceDeclare>,
    scopeFactory: Builder<Scope>
): DynamicApplicationContext, InitializeAware {

    override fun <T : Any> getService(declareType: KClass<T>): T {
        val serviceDeclare = declarations.findByDeclareType(declareType)
        return entry.getOrCreateService(serviceDeclare)
    }

    override fun scopeAbility(): ScopeAbility {
        return scopeTrace
    }

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) {
        declarations.addSingleton(type, lazy)
    }

    override fun <TD : Any, TI : Any> addSingleton(
        declarationType: KClass<TD>,
        implementationType: KClass<TI>,
        lazy: Boolean
    ) {
        declarations.addSingleton(declarationType, implementationType, lazy)
    }

    override fun <T : Any> addScoped(type: KClass<T>, lazy: Boolean) {
        declarations.addScoped(type, lazy)
    }

    override fun <TD : Any, TI : Any> addScoped(
        declarationType: KClass<TD>,
        implementationType: KClass<TI>,
        lazy: Boolean
    ) {
        declarations.addScoped(declarationType, implementationType, lazy)
    }

    override fun <T : Any> addTransient(type: KClass<T>) {
        declarations.addTransient(type)
    }

    override fun <TD : Any, TI : Any> addTransient(declarationType: KClass<TD>, implementationType: KClass<TI>) {
        declarations.addTransient(declarationType, implementationType)
    }

    override fun <TD : Any, TI : Any> replaceImplementation(
        declarationType: KClass<TD>,
        implementationType: KClass<TI>,
        lifecycle: Lifecycle?
    ) {
        declarations.replaceImplementation(declarationType, implementationType, lifecycle)
    }

    override fun onInit() {
        declarations = ServiceDeclares(_declarations)
        entry = ServiceEntry(declarations, storage, scopeTrace)
        declarations.forEach {
            if (!it.isLazyService && it.lifecycle == Lifecycle.Singleton) {
                getService(it.declarationTypes[0])
            }
        }
    }

    /**
     * the declaration of the services.
     */
    private lateinit var declarations: MutableServiceDeclares

    /**
     * the entry to get the service.
     */
    private lateinit var entry: ServiceEntry

    /**
     * the trace of the scope.
     */
    private val scopeTrace = ScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedServiceStorage()
}