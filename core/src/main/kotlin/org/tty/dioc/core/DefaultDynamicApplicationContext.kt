package org.tty.dioc.core

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.lifecycle.StackScopeTrace
import org.tty.dioc.core.storage.CombinedComponentStorage
import org.tty.dioc.core.internal.ComponentResolverImpl
import org.tty.dioc.base.Builder
import org.tty.dioc.core.identifier.ComponentIdentifier
import org.tty.dioc.observable.channel.observe
import kotlin.reflect.KClass

/**
 * the default implementation for dynamicApplication
 * @see [DynamicApplicationContext]
 */
open class DefaultDynamicApplicationContext(
    /**
     * the declaration of the services.
     */
    private val declarations: MutableComponentDeclares,
    scopeFactory: Builder<Scope>
): DynamicApplicationContext {

    override fun <T : Any> getComponent(declareType: KClass<T>): T {
        if (!initialized) {
            throw IllegalStateException("you must initialized it before getService.")
        }
        val serviceDeclare = declarations.singleDeclarationType(declareType)
        return entry.resolve(serviceDeclare)
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

    override fun forceReplace(action: (ComponentDeclareAware) -> Unit) {
        declarations.forceReplace(action)
    }

    @Suppress("DuplicatedCode")
    override fun onInit() {
        initialized = true
        entry = ComponentResolverImpl(declarations, storage, stackScopeTrace)
        declarations.forEach {
            if (!it.isLazyComponent && it.lifecycle == Lifecycle.Singleton) {
                getComponent(it.declarationTypes[0])
            }
        }

        stackScopeTrace.createChannel.observe(this::onCreateScope)
        stackScopeTrace.removeChannel.observe(this::onRemoveScope)
        declarations.createLazyChannel.observe(this::onCreateLazy)
    }


    private var initialized = false

    /**
     * the entry to get the service.
     */
    private lateinit var entry: ComponentResolverImpl

    /**
     * the trace of the scope.
     */
    private val stackScopeTrace = StackScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedComponentStorage()

    /**
     * the function callback after create a lazy service.
     */
    private fun onCreateLazy(createLazy: MutableComponentDeclares.CreateLazy) {
        val (declarationType, lifecycle, lazy) = createLazy
        if (!lazy) {
            if (lifecycle == Lifecycle.Singleton) {
                getComponent(declarationType)
            } else if (stackScopeTrace.currentScope() != null && lifecycle == Lifecycle.Scoped) {
                getComponent(declarationType)
            }
        }
    }

    /**
     * the function callback after create a scope.
     */
    @Suppress("UNUSED_PARAMETER")
    private fun onCreateScope(scope: Scope) {
        declarations.forEach {
            if (!it.isLazyComponent && it.lifecycle == Lifecycle.Scoped) {
                getComponent(it.declarationTypes[0])
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
                    ComponentIdentifier.ofDeclare(
                        it,
                        scope
                    )
                )
            }
        }
    }
}