package org.tty.dioc.core

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ReadonlyServiceDeclares
import org.tty.dioc.core.lifecycle.*
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.base.Builder
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.observable.channel.observe
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
    ) : ApplicationContext, InitializeAware, FinishAware {

    /**
     * the entry to get the service.
     */
    private lateinit var entry: ServiceEntry

    /**
     * the trace of the scope.
     */
    private val stackScopeTrace: ScopeAbility = StackScopeTrace(scopeFactory)

    /**
     * the storage of the services.
     */
    private val storage = CombinedServiceStorage()

    /**
     * root function to get the service by [declareType]
     */
    override fun <T : Any> getService(declareType: KClass<T>): T {
        val serviceDeclare = declarations.singleDeclarationType(declareType)
        return entry.resolve(serviceDeclare)
    }

    /**
     * the ability of the [Scope]
     */
    override fun scopeAbility(): ScopeAbility {
        return stackScopeTrace
    }

    @Suppress("DuplicatedCode")
    override fun onInit() {
        entry = ServiceEntry(declarations, storage, stackScopeTrace)
        declarations.forEach {
            if (!it.isLazyService && it.lifecycle == Lifecycle.Singleton) {
                getService(it.declarationTypes[0])
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
            if (!it.isLazyService && it.lifecycle == Lifecycle.Scoped) {
                getService(it.declarationTypes[0])
            }
        }
    }

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
    //endregion

}