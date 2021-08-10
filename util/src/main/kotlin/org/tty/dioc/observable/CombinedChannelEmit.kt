package org.tty.dioc.observable

class CombinedChannelEmit<T>(private val channels: List<ChannelEmit<T>>): ChannelEmit<T> {

    override fun emit(data: T) {
        channels.forEach {
            it.emit(data)
        }
    }

    override fun throws(throwable: Throwable) {
        channels.forEach {
            it.throws(throwable)
        }
    }
}