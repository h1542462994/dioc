package org.tty.dioc.observable

/**
 * a channel to send data
 * @param T the data type
 */
interface ChannelReceive<T> {
    /**
     * to receive the channel
     */
    fun receive(receiver: ChannelReceiver1<T>)

    /**
     * to receive the channel
     * @param TE the exception want to catch
     */
    fun <TE: Throwable> receive(receiver: ChannelReceiver2<T, TE>)
}