package org.tty.dioc.observable

interface ChannelReceiver2<T, TE: Throwable>: ChannelReceiver1<T> {
    fun onFail(throwable: TE): Boolean
}