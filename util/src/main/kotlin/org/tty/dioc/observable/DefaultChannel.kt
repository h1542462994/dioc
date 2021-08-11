package org.tty.dioc.observable

/**
 * default channel implementation
 */
class DefaultChannel<T>: ChannelFull<T> {
    private val channels: ArrayList<ChannelEmit<T>> = ArrayList()
    private val receivers: ArrayList<ChannelReceiver<T>> = ArrayList()
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
        if (receivers.isEmpty()) {
            chains.add(finalEmit)
            return finalEmit
        }

        // else to create receiver invoke chain.
        require(index in receivers.indices)

        val nextEmit: ChannelEmit<T> =

        if (index == receivers.size - 1){
            finalEmit
        } else {
            createNextAll(index + 1)
        }

        // the real chain element.
        val emit = object: ChannelEmit<T> {
            override fun emit(data: T) {
                // fixed bug
                // use should call the receiver.
                receivers[index].receive(data, nextEmit)
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

    override fun receive(receiver: ChannelReceiver<T>): Channel<T> {
        prepared = false
        receivers.add(receiver)
        return this
    }

    override fun removeReceiver(receiver: ChannelReceiver<T>): Channel<T> {
        prepared = false
        receivers.remove(receiver)
        return this
    }

    override fun cleanReceivers(): Channel<T> {
        prepared = false
        receivers.clear()
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