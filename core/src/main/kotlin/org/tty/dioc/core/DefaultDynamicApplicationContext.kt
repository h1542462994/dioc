package org.tty.dioc.core

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.lifecycle.StackScopeTrace
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.base.Builder
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.observable.channel.observe
import kotlin.io.path.createTempDirectory
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
        if (!initialized) {
            throw IllegalStateException("you must initialized it before getService.")
        }
        val serviceDeclare = declarations.singleDeclarationType(declareType)
        return entry.getOrCreateService(serviceDeclare)
    }

    override fun scopeAbility(): ScopeAbility {
        return stackScopeTrace
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

    @Suppress("DuplicatedCode")
    override fun onInit() {
        initialized = true
        entry = ServiceEntry(declarations, storage, stackScopeTrace)
        declarations.forEach {
            if (!it.isLazyService && it.lifecycle == Lifecycle.Singleton) {
                getService(it.declarationTypes[0])
            }
        }

        stackScopeTrace.createChannel().observe(this::onCreateScope)
        stackScopeTrace.removeChannel().observe(this::onRemoveScope)
        declarations.createLazyChannel().observe(this::onCreateLazy)
    }


    private var initialized = false

    /**
     * the entry to get the service.
     */
    private lateinit var entry: ServiceEntry

    /**
     * the trace of the scope.
     */
    private val stackScopeTrace = StackScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedServiceStorage()

    /**
     * the function callback after create a lazy service.
     */
    private fun onCreateLazy(createLazy: MutableServiceDeclares.CreateLazy) {
        val (declarationType, lifecycle, lazy) = createLazy
        if (!lazy) {
            if (lifecycle == Lifecycle.Singleton) {
                getService(declarationType)
            } else if (stackScopeTrace.currentScope() != null && lifecycle == Lifecycle.Scoped) {
                getService(declarationType)
            }
        }
    }

    /**
     * the function callback after create a scope.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onCreateScope(scope: Scope) {
        declarations.forEach {
            if (!it.isLazyService && it.lifecycle == Lifecycle.Scoped) {
                getService(it.declarationTypes[0])
            }
        }
    }

    /**
     * the function callback after remove a scope.
     */
    private fun onRemoveScope(scope: Scope) {
        declarations.forEach {
            if (it.lifecycle == Lifecycle.Scoped) {
                storage.remove(
                    ServiceIdentifier.ofDeclare(
                        it,
                        scope
                    )
                )
            }
        }
    }
}