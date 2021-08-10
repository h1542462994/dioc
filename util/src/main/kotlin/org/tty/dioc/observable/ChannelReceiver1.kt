package org.tty.dioc.observable

interface ChannelReceiver1<T> {
    fun onSuccess(data: T, nextChannelEmit: ChannelEmit<T>)
}