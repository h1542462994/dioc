package org.tty.dioc.core

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.lifecycle.*
import org.tty.dioc.core.internal.CombinedComponentStorage
import org.tty.dioc.core.internal.ComponentResolverImpl
import org.tty.dioc.base.FinishAware
import org.tty.dioc.base.InitializeAware
import org.tty.dioc.core.basic.ScopeAbility
import org.tty.dioc.core.basic.ScopeFactory
import org.tty.dioc.observable.channel.observe
import kotlin.reflect.KClass

/**
 * the default implementation for applicationContext
 * @see [ApplicationContext]
 */
@Deprecated("DefaultApplicationContext is replaced with startKernel.")
open class DefaultApplicationContext(
    /**
     * the declaration of the services.
     */
    private val declarations: ComponentDeclares,
    scopeFactory: ScopeFactory
    ) : ApplicationContext, InitializeAware, FinishAware {

    /**
     * the entry to get the service.
     */
    private lateinit var entry: ComponentResolverImpl

    /**
     * the trace of the scope.
     */
    private val stackScopeTrace: ScopeAbility = StackScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedComponentStorage()

    /**
     * root function to get the service by [indexType]
     */
    override fun <T: Any> getComponent(indexType: KClass<T>): T {
        val serviceDeclare = declarations.singleIndexType(indexType)
        return entry.resolve(serviceDeclare)
    }

    override fun <T : Any> getComponent(name: String, indexType: KClass<T>): T {
        TODO("Not yet implemented")
    }

    /**
     * the ability of the [Scope]
     */
    override fun scopeAbility(): ScopeAbility {
        return stackScopeTrace
    }

    @Suppress("DuplicatedCode")
    override fun onInit() {
        entry = ComponentResolverImpl(declarations, storage, stackScopeTrace)
        declarations.forEach {
            if (!it.isLazyComponent && it.lifecycle == Lifecycle.Singleton) {
                getComponent(it.indexTypes[0])
            }
        }

        stackScopeTrace.createChannel.observe(this::onCreateScope)
        stackScopeTrace.removeChannel.observe(this::onRemoveScope)
    }

    override fun onFinish() {

    }

    //region events
    @Suppress("UNUSED_PARAMETER")
    private fun onCreateScope(scope: Scope) {
        declarations.forEach {
            if (!it.isLazyComponent && it.lifecycle == Lifecycle.Scoped) {
                getComponent(it.indexTypes[0])
            }
        }
    }

    private fun onRemoveScope(scope: Scope) {
        declarations.forEach {
            if (it.lifecycle == Lifecycle.Scoped) {
                storage.remove(
                    it.createKey(scope)
                )
            }
        }
    }
    //endregion

}