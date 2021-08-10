package org.tty.dioc.observable

interface ChannelEmit<T> {
    fun emit(data: T)
    fun throws(throwable: Throwable)
}