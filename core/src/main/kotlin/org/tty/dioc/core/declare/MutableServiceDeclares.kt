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

    fun createLazyChannel(): Channel<CreateLazy>
}