package org.tty.dioc.observable

/**
 * a channel to receive the data,
 * replacement for method callback.
 * split the event sender, event channel and event receiver.
 */
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
     * @return the next channel created by default.
     */
    fun next(): Channel<T>

    /**
     * to next channel
     * @param channel the next channel received
     */
    fun next(channel: ChannelEmit<T>)

    /**
     * remove the receiver
     * @param receiver the removed receiver
     */
    fun removeReceiver(receiver: ChannelReceiver<T>): Channel<T>

    /**
     * to clean all receivers
     */
    fun cleanReceivers(): Channel<T>

    /**
     * clean all channels
     */
    fun cleanChannels(): Channel<T>
}
