package org.tty.dioc.observable.channel

import org.tty.dioc.observable.channel.contract.Channel
import org.tty.dioc.observable.channel.contract.ChannelEmit
import org.tty.dioc.observable.channel.contract.ChannelFull
import org.tty.dioc.observable.channel.contract.ChannelInterceptor

/**
 * default channel implementation
 */
internal class DefaultChannel<T>: ChannelFull<T> {
    private val channels: ArrayList<ChannelEmit<T>> = ArrayList()
    private val interceptors: ArrayList<ChannelInterceptor<T>> = ArrayList()
    private val mapping: ArrayList<ChannelMapping<T, *>> = ArrayList()
    private var prepared = false

    private lateinit var finalEmit: CombinedChannelEmit<T>
    private val chains: ArrayList<ChannelEmit<T>> = ArrayList()

    /**
     * ensure the [finalEmit] and [chains] is prepared.
     */
    private fun ensurePrepared() {
        if (!prepared) {
            prepared = true
            finalEmit = CombinedChannelEmit(
                channels.plus(mapping)
            )
            chains.clear()
            createNextAll(0)
        }
    }

    /**
     * get the next emit.
     * and construct the [chains]
     */
    private fun createNextAll(index: Int): ChannelEmit<T> {
        // if receivers is empty, return combinedChannelEmit directly.
        if (interceptors.isEmpty()) {
            chains.add(finalEmit)
            return finalEmit
        }

        // else to create receiver invoke chain.
        require(index in interceptors.indices)

        val nextEmit: ChannelEmit<T> =

        if (index == interceptors.size - 1){
            finalEmit
        } else {
            createNextAll(index + 1)
        }

        // the real chain element.
        val emit = object: ChannelEmit<T> {
            override fun emit(data: T) {
                // fixed bug
                // use should call the receiver.
                interceptors[index].intercept(data, nextEmit)
            }
        }

        // fixed bug: the last is first created.
        chains.add(0, emit)

        return emit
    }


    override fun <TR: Any> map(mapper: (T) -> TR): Channel<TR> {
        prepared = false
        val next = DefaultChannel<TR>()
        this.mapping.add(
            ChannelMapping(
                mapper,
                next
            )
        )
        return next
    }

    override fun next(): Channel<T> {
        prepared = false
        val channel = DefaultChannel<T>()
        channels.add(channel)
        return channel
    }

    override fun next(channel: ChannelEmit<T>) {
        prepared = false
        channels.add(channel)
    }

    override fun intercept(interceptor: ChannelInterceptor<T>): Channel<T> {
        prepared = false
        interceptors.add(interceptor)
        return this
    }

    override fun removeInterceptor(interceptor: ChannelInterceptor<T>): Channel<T> {
        prepared = false
        interceptors.remove(interceptor)
        return this
    }

    override fun removeChannelEmit(channelEmit: ChannelEmit<T>): Channel<T> {
        prepared = false
        channels.remove(channelEmit)
        return this
    }

    override fun cleanInterceptors(): Channel<T> {
        prepared = false
        interceptors.clear()
        return this
    }

    override fun cleanChannels(): Channel<T> {
        prepared = false
        channels.clear()
        return this
    }

    override fun emit(data: T) {
        ensurePrepared()

        chains.first().emit(data)
    }
}