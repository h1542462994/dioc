package org.tty.dioc.core.declare

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.core.scope.Scope
import kotlin.reflect.KClass

/**
 * declare contract for the component.
 */
interface IComponentDeclare {
    /**
     * [name] of the component, if name is empty, it's an **anonymous** component.
     */
    val name: String

    /**
     * [indexTypes] of the component, the **type key** of the component
     */
    val indexTypes: List<KClass<*>>

    /**
     * [lifecycle] of the component.
     */
    val lifecycle: Lifecycle

    /**
     * whether the component is build **by lazy**.
     */
    val lazyBuild: Boolean

    /**
     * create the [ComponentKey] by itself and [scope]
     */
    fun createKey(scope: Scope): ComponentKey
}