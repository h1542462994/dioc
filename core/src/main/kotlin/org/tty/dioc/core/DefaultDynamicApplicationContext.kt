package org.tty.dioc.core

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.lifecycle.ScopeTrace
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.observable.intercept
import org.tty.dioc.util.Builder
import kotlin.reflect.KClass

/**
 * the default implementation for dynamicApplication
 * @see [DynamicApplicationContext]
 */
open class DefaultDynamicApplicationContext(
    /**
     * the declaration of the services.
     */
    private val declarations: MutableServiceDeclares,
    scopeFactory: Builder<Scope>
): DynamicApplicationContext, InitializeAware {

    override fun <T : Any> getService(declareType: KClass<T>): T {
        val serviceDeclare = declarations.singleDeclarationType(declareType)
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

    override fun forceReplace(action: (ServiceDeclareAware) -> Unit) {
        declarations.forceReplace(action)
    }

    override fun onInit() {
        entry = ServiceEntry(declarations, storage, scopeTrace)
        declarations.forEach {
            if (!it.isLazyService && it.lifecycle == Lifecycle.Singleton) {
                getService(it.declarationTypes[0])
            }
        }
        scopeTrace.createChannel().intercept { _, _ ->
           declarations.forEach {
               if (!it.isLazyService && it.lifecycle == Lifecycle.Scoped) {
                   getService(it.declarationTypes[0])
               }
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
    private val scopeTrace = ScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedServiceStorage()
}