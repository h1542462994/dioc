package org.tty.dioc.observable


/**
 * lambda version to receive the channel
 * @param func the equal represent to [ChannelReceiver]
 */
fun <T> Channel<T>.receive(func: (data: T, next: (T) -> Unit) -> Unit): Channel<T> {
    return this.receive(object: ChannelReceiver<T> {
        override fun receive(data: T, next: ChannelEmit<T>) {
            func(data, next::emit)
        }
    })
}