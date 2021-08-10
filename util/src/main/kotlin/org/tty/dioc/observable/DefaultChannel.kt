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
        require(index in receivers.indices)

        val nextEmit: ChannelEmit<T> =
        if (index in 0 until receivers.size - 1){
            createNextAll(index + 1)
        } else {
            combinedChannelEmit
        }

        val emit = object: ChannelEmit<T> {
            override fun emit(data: T) {
                nextEmit.emit(data)
            }
        }

        nextAll.add(emit)
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
    override fun next(channel: ChannelEmit<T>) {
        channels.add(channel)
    }

    override fun receive(receiver: ChannelReceiver<T>): Channel<T> {
        receivers.add(receiver)
        return this
    }

    override fun emit(data: T) {
        ensurePrepared()

        if (receivers.isNotEmpty()) {
            receivers.first().receive(
                data, nextAll.first()
            )
        } else {
            combinedChannelEmit.emit(data)
        }
    }
}