package org.tty.dioc.observable

internal class CombinedChannelEmit<T>(private val channels: List<ChannelEmit<T>>): ChannelEmit<T> {
    override fun emit(data: T) {
        channels.forEach {
            it.emit(data)
        }
    }
}