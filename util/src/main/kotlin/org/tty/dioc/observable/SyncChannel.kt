package org.tty.dioc.observable

/**
 * the synchronized channel with [channels]
 * a descriptor for [Channel]
 */
class SyncChannel<T>(private val channels: List<Channel<T>>): Channel<ArrayList<T>> {
    private val dataCollect = ArrayList<ArrayList<T>>()
    private val channel = Channels.create<ArrayList<T>>()

    init {
        require(channels.isNotEmpty())
        ensurePrepared()
        channels.forEachIndexed { index, channel ->
            channel.next(object: ChannelEmit<T> {
                override fun emit(data: T) {
                    dataCollect[index].add(data)
                    checkAndEmitToChannel()
                }
            })
        }
    }

    private fun ensurePrepared() {
        for (i in channels.indices) {
            dataCollect.add(ArrayList())
        }
    }

    private fun checkAndEmitToChannel() {
        val countMax = dataCollect.map { it.size }.maxOrNull()
        require(countMax != null)
        while (countMax > 0) {
            val data = extractData()
            channel.emit(data)
        }
    }

    private fun extractData(): ArrayList<T> {
        val data = ArrayList<T>()
        dataCollect.forEach {
            data.add(it.first())
            data.removeAt(0)
        }
        return data
    }

    override fun <TR : Any> map(mapper: (ArrayList<T>) -> TR): Channel<TR> {
        return channel.map(mapper)
    }

    override fun receive(receiver: ChannelReceiver<ArrayList<T>>): Channel<ArrayList<T>> {
        return channel.receive(receiver)
    }

    override fun next(): Channel<ArrayList<T>> {
        return channel.next()
    }

    override fun next(channel: ChannelEmit<ArrayList<T>>) {
        this.channel.next(channel)
    }

    override fun removeReceiver(receiver: ChannelReceiver<ArrayList<T>>): Channel<ArrayList<T>> {
        return channel.removeReceiver(receiver)
    }

    override fun cleanReceivers(): Channel<ArrayList<T>> {
        return channel.cleanReceivers()
    }

    override fun cleanChannels(): Channel<ArrayList<T>> {
        return channel.cleanChannels()
    }

}