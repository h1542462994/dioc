package org.tty.dioc.observable

interface ChannelReceiver<T> {
    /**
     * to receive the data
     */
    fun receive(data: T, next: ChannelEmit<T>)
}