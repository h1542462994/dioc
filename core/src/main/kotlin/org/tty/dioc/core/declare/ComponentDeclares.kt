
package org.tty.dioc.core.declare

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.error.ServiceDeclarationException
import org.tty.dioc.core.util.ServiceUtil
import org.tty.dioc.observable.channel.Channels
import org.tty.dioc.util.formatTable
import kotlin.reflect.KClass

/**
 * a implementation of [MutableComponentDeclares] and [ReadonlyComponentDeclares]
 */
class ComponentDeclares() : MutableComponentDeclares, ReadonlyComponentDeclares {
//    constructor(componentDeclares: List<ComponentDeclare>) : this() {
//        container.addAll(componentDeclares)
//    }

    private val container = ArrayList<ComponentDeclare>()
    private var forceReplaceEnabled = false

    override fun addAll(componentDeclares: List<ComponentDeclare>) {
        container.addAll(componentDeclares)
    }

    override val createLazyChannel = Channels.create<MutableComponentDeclares.CreateLazy>()

    override fun iterator(): Iterator<ComponentDeclare> {
        return container.iterator()
    }

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) {
        // use delegate.
        addDeclareByType(type, type, lifecycle = Lifecycle.Singleton, lazy)
        onCreateLazy(type, lifecycle = Lifecycle.Singleton, lazy)
    }

    override fun <TD : Any, TI : Any> addSingleton(
        declarationType: KClass<TD>,
        implementationType: KClass<TI>,
        lazy: Boolean
    ) {
        // use delegate.
        addDeclareByType(declarationType, implementationType, lifecycle = Lifecycle.Singleton, lazy)
        onCreateLazy(declarationType, lifecycle = Lifecycle.Singleton, lazy)
    }

    override fun <T : Any> addScoped(type: KClass<T>, lazy: Boolean) {
        // use delegate.
        addDeclareByType(type, type, lifecycle = Lifecycle.Scoped, lazy)
        onCreateLazy(type, lifecycle = Lifecycle.Singleton, lazy)
    }

    override fun <TD : Any, TI : Any> addScoped(declarationType: KClass<TD>, implementationType: KClass<TI>, lazy: Boolean) {
        // use delegate.
        addDeclareByType(declarationType, implementationType, lifecycle = Lifecycle.Scoped, lazy)
        onCreateLazy(declarationType, lifecycle = Lifecycle.Scoped, lazy)
    }

    override fun <T : Any> addTransient(type: KClass<T>) {
        // use delegate.
        addDeclareByType(type, type, lifecycle = Lifecycle.Transient, true)
    }

    override fun <TD : Any, TI : Any> addTransient(declarationType: KClass<TD>, implementationType: KClass<TI>) {
        // use delegate.
        addDeclareByType(declarationType, implementationType, lifecycle = Lifecycle.Transient, true)
    }

    override fun forceReplace(action: (ComponentDeclareAware) -> Unit) {
        forceReplaceEnabled = true
        action.invoke(this)
        forceReplaceEnabled = false
    }

    override fun singleDeclarationTypeOrNull(declarationType: KClass<*>): ComponentDeclare? {
        return this.singleOrNull { it.indexTypes.contains(declarationType) }
    }

    /**
     * find in collection where [ComponentDeclare.indexTypes] contains [declarationType]
     */
    override fun singleDeclarationType(declarationType: KClass<*>): ComponentDeclare {
        return this.single { it.indexTypes.contains(declarationType) }
    }

    /**
     * find in collection where [ComponentDeclare.implementationType] == [implementationType]
     */
    override fun singleServiceType(implementationType: KClass<*>): ComponentDeclare {
        return this.single { it.implementationType == implementationType }
    }

    /**
     * add a [declarationType] with [implementationType] to [container].
     * the structure is determined by [implementationType] itself.
     */
    private fun addDeclareByType(declarationType: KClass<*>, implementationType: KClass<*>, lifecycle: Lifecycle, lazy: Boolean) {
        val l = singleDeclarationTypeOrNull(declarationType)
        // remove the existed declaration
        if (l != null && forceReplaceEnabled) {
            container.removeIf {
                it.indexTypes.contains(declarationType)
            }
        }


        if (l == null || forceReplaceEnabled) {
            container.add(
                ComponentDeclare(
                    name = null,
                    implementationType = implementationType,
                    indexTypes = listOf(declarationType),
                    lifecycle = lifecycle,
                    isLazyComponent = lazy,
                    constructor = ServiceUtil.getInjectConstructor(implementationType),
                    components = ServiceUtil.getComponents(implementationType)
                )
            )
        } else {
            throw ServiceDeclarationException("the declaration of the type $declarationType is redundant.")
        }
    }

    /**
     * to check the structure of the service on the current [componentDeclare]
     */
    override fun check(componentDeclare: ComponentDeclare) {
        if (componentDeclare.lifecycle == Lifecycle.Transient && !componentDeclare.isLazyComponent) {
            throw ServiceDeclarationException("the transient service must be a lazy service.")
        } else {
            componentDeclare.components.forEach {
                val aDeclare = this.singleDeclarationTypeOrNull(it.declareType)
                if (aDeclare != null && aDeclare.lifecycle == Lifecycle.Scoped && !it.injectLazy) {
                    throw ServiceDeclarationException("you must inject a scoped service by @Lazy")
                }
            }
        }
    }

    private fun onCreateLazy(declarationType: KClass<*>, lifecycle: Lifecycle, lazy: Boolean = true) {
        createLazyChannel.emit(
            MutableComponentDeclares.CreateLazy(declarationType, lifecycle, lazy)
        )
    }

    override fun toString(): String {
        return formatTable("componentDeclares", container, title = listOf("lifecycle", "isLazyComponent", "declarationTypes", "implementationType")) {
            listOf(
                it.lifecycle,
                it.isLazyComponent,
                it.indexTypes,
                it.implementationType,
            )
        }.toString()
    }
}