package org.tty.dioc.observable.channel.contract

/**
 * a channel to receive the data,
 * replacement for method callback.
 * split the event sender, event channel and event receiver.
 */
interface Channel<T> {
    /**
     * map the channel.
     */
    fun <TR: Any> map(mapper: (T) -> TR): Channel<TR>

    /**
     * to intercept the channel.
     */
    fun intercept(interceptor: ChannelInterceptor<T>): Channel<T>

    /**
     * to next channel
     * @return the next channel created by default.
     */
    fun next(): Channel<T>

    /**
     * to next channel
     * @param channel the next channel received.
     */
    fun next(channel: ChannelEmit<T>)

    /**
     * remove the intercept
     * @param interceptor the removed interceptor.
     */
    fun removeInterceptor(interceptor: ChannelInterceptor<T>): Channel<T>

    /**
     * remove the channel emit
     * @param channelEmit the removed channelEmit.
     */
    fun removeChannelEmit(channelEmit: ChannelEmit<T>): Channel<T>

    /**
     * to clean all interceptors.
     */
    fun cleanInterceptors(): Channel<T>

    /**
     * clean all channels.
     */
    fun cleanChannels(): Channel<T>
}
