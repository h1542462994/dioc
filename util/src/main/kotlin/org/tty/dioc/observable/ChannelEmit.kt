package org.tty.dioc.observable

interface ChannelEmit<T> {
    /**
     * emit the data to [Channel]
     */
    fun emit(data: T)
}