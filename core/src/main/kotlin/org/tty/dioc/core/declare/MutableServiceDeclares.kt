package org.tty.dioc.core.declare

import org.tty.dioc.observable.channel.contract.Channel
import kotlin.reflect.KClass

/**
 * mutable service declares
 * @see [ServiceDeclares]
 */
interface MutableServiceDeclares: ReadonlyServiceDeclares, ServiceDeclareAware {
    data class CreateLazy(
        val declarationType: KClass<*>,
        val lifecycle: Lifecycle,
        val lazy: Boolean = true
    )

    /**
     * the channel for create a lazy service
     * @see Service
     */
    val createLazyChannel: Channel<CreateLazy>
}