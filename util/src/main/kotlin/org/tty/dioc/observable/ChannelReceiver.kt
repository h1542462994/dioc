package org.tty.dioc.observable

/**
 * the receiver for the channel.
 */
interface ChannelReceiver<T> {
    /**
     * to receive the data.
     * @param data the received data.
     * @param next the next channel emit.
     */
    fun receive(data: T, next: ChannelEmit<T>)
}