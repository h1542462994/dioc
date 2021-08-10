package org.tty.dioc.observable

data class ChannelMapping<T, TR>(
    val mapping: (T) -> TR,
    val channel: ChannelEmit<TR>
): ChannelEmit<T> {
    override fun emit(data: T) {
        val d = mapping(data)
        channel.emit(d)
    }
}
