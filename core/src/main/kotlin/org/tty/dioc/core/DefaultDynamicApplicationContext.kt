package org.tty.dioc.core

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.basic.ComponentDeclareAware
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.basic.ScopeAbility
import org.tty.dioc.core.lifecycle.StackScopeTrace
import org.tty.dioc.core.internal.CombinedComponentStorage
import org.tty.dioc.core.internal.ComponentResolverImpl
import org.tty.dioc.core.basic.ScopeFactory
import org.tty.dioc.observable.channel.observe
import kotlin.reflect.KClass

/**
 * the default implementation for dynamicApplication
 * @see [DynamicApplicationContext]
 */
@Deprecated("")
open class DefaultDynamicApplicationContext(
    /**
     * the declaration of the services.
     */
    private val declarations: ComponentDeclares,
    scopeFactory: ScopeFactory
): DynamicApplicationContext {

    override fun <T : Any> getComponent(indexType: KClass<T>): T {
        if (!initialized) {
            throw IllegalStateException("you must initialized it before getService.")
        }
        val serviceDeclare = declarations.singleIndexType(indexType)
        return entry.resolve(serviceDeclare)
    }

    override fun <T : Any> getComponent(name: String, indexType: KClass<T>): T {
        TODO("Not yet implemented")
    }

    override fun scopeAbility(): ScopeAbility {
        return stackScopeTrace
    }

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) {
        declarations.addSingleton(type, lazy)
    }

    override fun <T : Any> addSingleton(name: String, type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addSingleton(
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) {
        declarations.addSingleton(indexType, realType, lazy)
    }

    override fun <TD : Any, TI : Any> addSingleton(
        name: String,
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addScoped(type: KClass<T>, lazy: Boolean) {
        declarations.addScoped(type, lazy)
    }

    override fun <T : Any> addScoped(name: String, type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addScoped(
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) {
        declarations.addScoped(indexType, realType, lazy)
    }

    override fun <TD : Any, TI : Any> addScoped(
        name: String,
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> addTransient(type: KClass<T>) {
        declarations.addTransient(type)
    }

    override fun <T : Any> addTransient(name: String, type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addTransient(indexType: KClass<TD>, realType: KClass<TI>) {
        declarations.addTransient(indexType, realType)
    }

    override fun <TD : Any, TI : Any> addTransient(name: String, indexType: KClass<TD>, realType: KClass<TI>) {
        TODO("Not yet implemented")
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
                getComponent(it.indexTypes[0])
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
    private fun onCreateLazy(createLazy: ComponentDeclares.CreateLazy) {
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
                getComponent(it.indexTypes[0])
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
                    it.createKey(scope)
                )
            }
        }
    }
}