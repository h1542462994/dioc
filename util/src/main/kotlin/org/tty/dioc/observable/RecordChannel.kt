package org.tty.dioc.observable

import org.tty.dioc.base.InitVarargsR

internal class RecordChannel<T>(private val channels: Channel<T>): Channel<List<T>>, InitVarargsR<T, Channel<List<T>>> {
    override fun <TR : Any> map(mapper: (List<T>) -> TR): Channel<TR> {
        TODO("Not yet implemented")
    }

    override fun intercept(interceptor: ChannelInterceptor<List<T>>): Channel<List<T>> {
        TODO("Not yet implemented")
    }

    override fun next(): Channel<List<T>> {
        TODO("Not yet implemented")
    }

    override fun next(channel: ChannelEmit<List<T>>) {
        TODO("Not yet implemented")
    }

    override fun removeInterceptor(interceptor: ChannelInterceptor<List<T>>): Channel<List<T>> {
        TODO("Not yet implemented")
    }

    override fun removeChannelEmit(channelEmit: ChannelEmit<List<T>>): Channel<List<T>> {
        TODO("Not yet implemented")
    }

    override fun cleanInterceptors(): Channel<List<T>> {
        TODO("Not yet implemented")
    }

    override fun cleanChannels(): Channel<List<T>> {
        TODO("Not yet implemented")
    }

    override fun init(vararg args: T): Channel<List<T>> {
        TODO("Not yet implemented")
    }

}