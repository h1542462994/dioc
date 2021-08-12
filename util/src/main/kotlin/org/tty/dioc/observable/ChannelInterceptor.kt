package org.tty.dioc.observable

/**
 * the interceptor for the channel.
 */
interface ChannelInterceptor<T> {
    /**
     * to intercept the data.
     * @param data the intercepted data.
     * @param next the next channel emit.
     */
    fun intercept(data: T, next: ChannelEmit<T>)
}