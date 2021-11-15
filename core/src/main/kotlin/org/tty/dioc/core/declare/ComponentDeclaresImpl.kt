
package org.tty.dioc.core.declare

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.basic.ComponentDeclareAware
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.error.ServiceDeclarationException
import org.tty.dioc.core.util.ServiceUtil
import org.tty.dioc.observable.channel.Channels
import org.tty.dioc.util.formatTable
import kotlin.reflect.KClass

/**
 * a implementation of [ComponentDeclares]
 */
internal class ComponentDeclaresImpl : ComponentDeclares {
//    constructor(componentDeclares: List<ComponentDeclare>) : this() {
//        container.addAll(componentDeclares)
//    }

    private val container = ArrayList<ComponentDeclare>()
    private var forceReplaceEnabled = false

    override fun addAll(componentDeclares: List<ComponentDeclare>) {
        container.addAll(componentDeclares)
    }

    override val createLazyChannel = Channels.create<ComponentDeclares.CreateLazy>()

    override fun iterator(): Iterator<ComponentDeclare> {
        return container.iterator()
    }

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) {
        // use delegate.
        addDeclareByType("", type, type, lifecycle = Lifecycle.Singleton, lazy)
        onCreateLazy(type, lifecycle = Lifecycle.Singleton, lazy)
    }

    override fun <T : Any> addSingleton(name: String, type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addSingleton(
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) {
        // use delegate.
        addDeclareByType("", indexType, realType, lifecycle = Lifecycle.Singleton, lazy)
        onCreateLazy(indexType, lifecycle = Lifecycle.Singleton, lazy)
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
        // use delegate.
        addDeclareByType("", type, type, lifecycle = Lifecycle.Scoped, lazy)
        onCreateLazy(type, lifecycle = Lifecycle.Singleton, lazy)
    }

    override fun <T : Any> addScoped(name: String, type: KClass<T>, lazy: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addScoped(indexType: KClass<TD>, realType: KClass<TI>, lazy: Boolean) {
        // use delegate.
        addDeclareByType("", indexType, realType, lifecycle = Lifecycle.Scoped, lazy)
        onCreateLazy(indexType, lifecycle = Lifecycle.Scoped, lazy)
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
        // use delegate.
        addDeclareByType("", type, type, lifecycle = Lifecycle.Transient, true)
    }

    override fun <T : Any> addTransient(name: String, type: KClass<T>) {
        TODO("Not yet implemented")
    }

    override fun <TD : Any, TI : Any> addTransient(indexType: KClass<TD>, realType: KClass<TI>) {
        // use delegate.
        addDeclareByType("", indexType, realType, lifecycle = Lifecycle.Transient, true)
    }

    override fun <TD : Any, TI : Any> addTransient(name: String, indexType: KClass<TD>, realType: KClass<TI>) {
        TODO("Not yet implemented")
    }

    override fun forceReplace(action: (ComponentDeclareAware) -> Unit) {
        forceReplaceEnabled = true
        action.invoke(this)
        forceReplaceEnabled = false
    }

    override fun singleIndexTypeOrNull(indexType: KClass<*>): ComponentDeclare? {
        return this.singleOrNull { it.indexTypes.contains(indexType) }
    }

    override fun singleName(name: String): ComponentDeclare {
        TODO("Not yet implemented")
    }

    override fun singleNameOrNull(name: String): ComponentDeclare? {
        TODO("Not yet implemented")
    }

    /**
     * find in collection where [ComponentDeclare.indexTypes] contains [indexType]
     */
    override fun singleIndexType(indexType: KClass<*>): ComponentDeclare {
        return this.single { it.indexTypes.contains(indexType) }
    }

    /**
     * find in collection where [ComponentDeclare.realType] == [realType]
     */
    override fun singleServiceType(realType: KClass<*>): ComponentDeclare {
        return this.single { it.realType == realType }
    }

    /**
     * add a [indexType] with [realType] to [container].
     * the structure is determined by [realType] itself.
     */
    private fun addDeclareByType(name: String, indexType: KClass<*>, realType: KClass<*>, lifecycle: Lifecycle, lazy: Boolean) {
        val l = singleIndexTypeOrNull(indexType)
        // remove the existed declaration
        if (l != null && forceReplaceEnabled) {
            container.removeIf {
                it.indexTypes.contains(indexType)
            }
        }


        if (l == null || forceReplaceEnabled) {
            container.add(
                ComponentDeclare(
                    name = name,
                    realType = realType,
                    indexTypes = listOf(indexType),
                    internal = false,
                    lifecycle = lifecycle,
                    isLazyComponent = lazy,
                    constructor = ServiceUtil.injectConstructor(realType),
                    components = ServiceUtil.components(realType)
                )
            )
        } else {
            throw ServiceDeclarationException("the declaration of the type $indexType is redundant.")
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
                val aDeclare = this.singleIndexTypeOrNull(it.declareType)
                if (aDeclare != null && aDeclare.lifecycle == Lifecycle.Scoped && !it.injectLazy) {
                    throw ServiceDeclarationException("you must inject a scoped service by @Lazy")
                }
            }
        }
    }

    private fun onCreateLazy(indexType: KClass<*>, lifecycle: Lifecycle, lazy: Boolean = true) {
        createLazyChannel.emit(
            ComponentDeclares.CreateLazy(indexType, lifecycle, lazy)
        )
    }

    override fun toString(): String {
        return formatTable("componentDeclares", container, title = listOf("lifecycle", "isLazyComponent", "declarationTypes", "implementationType")) {
            listOf(
                it.lifecycle,
                it.isLazyComponent,
                it.indexTypes,
                it.realType,
            )
        }.toString()
    }
}