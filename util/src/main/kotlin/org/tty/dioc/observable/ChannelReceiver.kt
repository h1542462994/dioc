package org.tty.dioc.observable

interface ChannelReceiver<T> {
    fun onSuccess(data: T, next: ChannelEmit<T>)
}