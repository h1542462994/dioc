package org.tty.dioc.observable

interface Channel<T> {
    /**
     * map the channel
     */
    fun <TR: Any> map(mapper: (T) -> TR): Channel<TR>

    /**
     * to receive the channel
     */
    fun receive(receiver: ChannelReceiver<T>): Channel<T>

    /**
     * to next channel
     */
    fun next(channel: ChannelEmit<T>)
}