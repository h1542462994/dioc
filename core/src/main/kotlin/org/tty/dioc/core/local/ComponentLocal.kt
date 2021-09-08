package org.tty.dioc.core.local

import org.tty.dioc.observable.channel.contract.Channel

/**
 * the local component, used on stack visitation.
 * you should use the component local carefully, because it breaks the ooc.
 */
interface ComponentLocal<T: Any> {
    /**
     * the holder for component.
     */
    interface Holder<T> {
        fun isLive(): Boolean
        fun get(): T
    }

    /**
     * provides the [DefaultComponentLocal] with [component]
     */
    infix fun provides(component: T)

    /**
     * provides the [DefaultComponentLocal] with [holderCall]
     * @see [HolderCall]
     */
    infix fun <TH: Any> provides(holderCall: HolderCall<TH, T>)

    /**
     * provides the [DefaultComponentLocal] with [holder] and [call]
     */
    fun <TH: Any> provides(holder: TH, call: (TH) -> T)

    /**
     * to pop the current [Holder].
     */
    fun pop()

    fun isEmpty(): Boolean

    /**
     * get the current available component of [DefaultComponentLocal]
     */
    val current: T

    val currentHolder: Holder<T>

    /**
     * add channel
     */
    val addChannel: Channel<Holder<T>>

    /**
     * remove channel
     */
    val removeChannel: Channel<Holder<T>>
}