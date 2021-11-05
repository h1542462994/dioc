package org.tty.dioc.core.declare

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.observable.channel.contract.Channel
import kotlin.reflect.KClass
import org.tty.dioc.annotation.Component

/**
 * mutable service declares
 * @see [ComponentDeclares]
 */

interface MutableComponentDeclares: ReadonlyComponentDeclares, ComponentDeclareAware {
    fun addAll(componentDeclares: List<ComponentDeclare>)

    data class CreateLazy(
        val declarationType: KClass<*>,
        val lifecycle: Lifecycle,
        val lazy: Boolean = true
    )

    /**
     * the channel for create a lazy service
     * @see Component
     */
    val createLazyChannel: Channel<CreateLazy>
}