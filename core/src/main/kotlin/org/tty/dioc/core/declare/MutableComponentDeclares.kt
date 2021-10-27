package org.tty.dioc.core.declare

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.observable.channel.contract.Channel
import kotlin.reflect.KClass

/**
 * mutable service declares
 * @see [ComponentDeclares]
 */
interface MutableComponentDeclares: ReadonlyComponentDeclares, ComponentDeclareAware {
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