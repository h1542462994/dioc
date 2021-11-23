package org.tty.dioc.core.internal

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.basic.ComponentDeclareAware
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.error.ServiceDeclarationException
import org.tty.dioc.core.util.ServiceUtil
import org.tty.dioc.observable.channel.Channels
import org.tty.dioc.util.formatTable
import kotlin.reflect.KClass

/**
 * an implementation of [ComponentDeclares]
 */
internal class ComponentDeclaresImpl : ComponentDeclares {
    private val container = ArrayList<ComponentDeclare>()
    private var forceReplaceEnabled = false

    override fun addAll(componentDeclares: List<ComponentDeclare>) {
        container.addAll(componentDeclares)
    }

    override val createEvent = Channels.create<ComponentDeclare>()
    override val removeEvent = Channels.create<ComponentDeclare>()

    override fun iterator(): Iterator<ComponentDeclare> {
        return container.iterator()
    }

    override fun <T : Any> addSingleton(type: KClass<T>, lazy: Boolean) = // use delegate.
        addDeclareByType("", type, type, lifecycle = Lifecycle.Singleton, lazy)

    override fun <T : Any> addSingleton(
        name: String,
        type: KClass<T>,
        lazy: Boolean
    ) = // use delegate.
        require(name.isNotEmpty()) {
            "name could not be empty."
        }.apply {
            addDeclareByType(name, type, type, lifecycle = Lifecycle.Singleton, lazy)
        }

    override fun <TD : Any, TI : Any> addSingleton(
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) = // use delegate.
        addDeclareByType("", indexType, realType, lifecycle = Lifecycle.Singleton, lazy)

    override fun <TD : Any, TI : Any> addSingleton(
        name: String,
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) = // use delegate.
        require(name.isNotEmpty()) {
            "name could not be empty."
        }.apply {
            addDeclareByType(name, indexType, realType, lifecycle = Lifecycle.Singleton, lazy)
        }

    override fun <T : Any> addScoped(type: KClass<T>, lazy: Boolean) = // use delegate.
        addDeclareByType("", type, type, lifecycle = Lifecycle.Scoped, lazy)

    override fun <T : Any> addScoped(
        name: String,
        type: KClass<T>,
        lazy: Boolean
    ) = // use delegate.
        require(name.isNotEmpty()) {
            "name could not be empty."
        }.apply {
            addDeclareByType(name, type, type, lifecycle = Lifecycle.Scoped, lazy)
        }

    override fun <TD : Any, TI : Any> addScoped(
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) =
        // use delegate.
        addDeclareByType("", indexType, realType, lifecycle = Lifecycle.Scoped, lazy)

    override fun <TD : Any, TI : Any> addScoped(
        name: String,
        indexType: KClass<TD>,
        realType: KClass<TI>,
        lazy: Boolean
    ) = // use delegate.
        require(name.isNotEmpty()) {
            "name could not be empty."
        }.apply {
            addDeclareByType(name, indexType, realType, lifecycle = Lifecycle.Scoped, lazy)
        }

    override fun <T : Any> addTransient(type: KClass<T>) = // use delegate.
        addDeclareByType("", type, type, lifecycle = Lifecycle.Transient, true)

    override fun <T : Any> addTransient(
        name: String,
        type: KClass<T>
    ) = // use delegate.
        require(name.isNotEmpty()) {
            "name could not be empty."
        }.apply {
            addDeclareByType(name, type, type, lifecycle = Lifecycle.Transient, true)
        }

    override fun <TD : Any, TI : Any> addTransient(
        indexType: KClass<TD>,
        realType: KClass<TI>
    ) = // use delegate.
        addDeclareByType("", indexType, realType, lifecycle = Lifecycle.Transient, true)

    override fun <TD : Any, TI : Any> addTransient(
        name: String,
        indexType: KClass<TD>,
        realType: KClass<TI>
    ) = // use delegate.
        require(name.isNotEmpty()) {
            "name could not be empty."
        }.apply {
            addDeclareByType(name, indexType, realType, lifecycle = Lifecycle.Transient, true)
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
        return this.single { it.name == name }
    }

    override fun singleNameOrNull(name: String): ComponentDeclare? {
        return this.singleOrNull { it.name == name }
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
    private fun addDeclareByType(
        name: String,
        indexType: KClass<*>,
        realType: KClass<*>,
        lifecycle: Lifecycle,
        lazy: Boolean
    ) {
        val e = singleIndexTypeOrNull(indexType)
        // remove the existed declaration
        if (e != null && forceReplaceEnabled) {
            container.removeIf {
                it.indexTypes.contains(indexType)
            }
            removeEvent.emit(e)
        }

        val d = ComponentDeclare(
            name = name,
            realType = realType,
            indexTypes = listOf(indexType),
            internal = false,
            lifecycle = lifecycle,
            isLazyComponent = lazy,
            constructor = ServiceUtil.injectConstructor(realType),
            components = ServiceUtil.components(realType)
        )

        if (e == null || forceReplaceEnabled) {
            container.add(d)
            createEvent.emit(d)
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

    override fun toString(): String {
        return formatTable(
            "componentDeclares",
            container,
            title = listOf("lifecycle", "isLazyComponent", "declarationTypes", "implementationType")
        ) {
            listOf(
                it.lifecycle,
                it.isLazyComponent,
                it.indexTypes,
                it.realType,
            )
        }.toString()
    }
}