package org.tty.dioc.observable.channel

import org.tty.dioc.observable.channel.contract.Channel
import org.tty.dioc.observable.channel.contract.ChannelEmit
import org.tty.dioc.observable.channel.contract.ChannelInterceptor


/**
 * lambda version to intercept the channel
 * @param func the equal represent to [ChannelInterceptor]
 * @return channel itself.
 */
fun <T> Channel<T>.intercept(
    func: (data: T, next: (T) -> Unit) -> Unit
): Channel<T> {
    return this.intercept(object: ChannelInterceptor<T> {
        override fun intercept(data: T, next: ChannelEmit<T>) {
            func(data, next::emit)
        }
    })
}

/**
 * observe the channel
 * @param func the equal represent to [ChannelEmit]
 * @return channel itself.
 */
fun <T> Channel<T>.observe(
    func: (data: T) -> Unit
): Channel<T> {
    val emit = object: ChannelEmit<T> {
        override fun emit(data: T) {
            func(data)
        }
    }

    this.next(emit)
    return this
}