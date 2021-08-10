package org.tty.dioc.observable

/**
 * default channel implementation
 */
class DefaultChannel<T>: Channel<T>, ChannelEmit<T> {
    private val channels: ArrayList<ChannelEmit<T>> = ArrayList()
    private val receivers: ArrayList<ChannelReceiver<T>> = ArrayList()
    private val mapping: ArrayList<ChannelMapping<T, *>> = ArrayList()
    private var prepared = false

    private lateinit var combinedChannelEmit: CombinedChannelEmit<T>
    private val nextAll: ArrayList<ChannelEmit<T>> = ArrayList()

    /**
     * ensure the [combinedChannelEmit] and [nextAll] is prepared.
     */
    private fun ensurePrepared() {
        if (!prepared) {
            prepared = true
            combinedChannelEmit = CombinedChannelEmit(
                channels.plus(mapping)
            )
            nextAll.clear()
            createNextAll(0)

        }
    }

    /**
     * get the next emit.
     */
    private fun createNextAll(index: Int): ChannelEmit<T> {
        // if receivers is empty, return combinedChannelEmit directly.
        if (receivers.isEmpty()) {
            nextAll.add(combinedChannelEmit)
            return combinedChannelEmit
        }

        // else to create receiver invoke chain.
        require(index in receivers.indices)

        val nextEmit: ChannelEmit<T> =

        if (index == receivers.size - 1){
            combinedChannelEmit
        } else {
            createNextAll(index + 1)
        }

        val emit = object: ChannelEmit<T> {
            override fun emit(data: T) {
                // fixed bug
                // use should call the receiver.
                receivers[index].receive(data, nextEmit)
            }
        }

        // fixed bug: the last is first created.
        nextAll.add(0, emit)

        return emit
    }


    override fun <TR: Any> map(mapper: (T) -> TR): Channel<TR> {
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
        val channel = Channels.create<T>()
        channels.add(channel)
        return channel
    }

    override fun next(channel: ChannelEmit<T>) {
        channels.add(channel)
    }

    override fun receive(receiver: ChannelReceiver<T>): Channel<T> {
        receivers.add(receiver)
        return this
    }

    override fun removeReceiver(receiver: ChannelReceiver<T>): Channel<T> {
        receivers.remove(receiver)
        return this
    }

    override fun emit(data: T) {
        ensurePrepared()

        nextAll.first().emit(data)
    }

    override fun cleanReceivers(): Channel<T> {
        receivers.clear()
        return this
    }

    override fun cleanChannels(): Channel<T> {
        channels.clear()
        return this
    }

}