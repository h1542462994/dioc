package org.tty.dioc.observable.channel.contract

interface ChannelEmit<T> {
    /**
     * emit the data to [Channel]
     */
    fun emit(data: T)
}