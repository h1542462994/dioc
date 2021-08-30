package org.tty.dioc.observable.channel

import org.tty.dioc.observable.channel.contract.*

/**
 * the synchronized channel with [channels]
 * a descriptor for [Channel]
 */
internal class SyncChannel<T>(private val channels: List<Channel<T>>) : Channel<List<T>>  {
    private val dataCollect = ArrayList<ArrayList<T>>()
    private val channel = Channels.create<List<T>>()

    init {
        require(channels.isNotEmpty())
        ensurePrepared()
        channels.forEachIndexed { index, channel ->
            channel.observe { data ->
                dataCollect[index].add(data)
                checkAndEmitToChannel()
            }
        }
    }

    //region copy functions

    override fun <TR : Any> map(mapper: (List<T>) -> TR): Channel<TR> = channel.map(mapper)
    override fun intercept(interceptor: ChannelInterceptor<List<T>>): Channel<List<T>> = channel.intercept(interceptor)
    override fun next(): Channel<List<T>> = channel.next()
    override fun next(channel: ChannelEmit<List<T>>) = this.channel.next(channel)
    override fun removeInterceptor(interceptor: ChannelInterceptor<List<T>>): Channel<List<T>> =
        channel.removeInterceptor(interceptor)

    override fun removeChannelEmit(channelEmit: ChannelEmit<List<T>>): Channel<List<T>> =
        channel.removeChannelEmit(channelEmit)

    override fun cleanInterceptors(): Channel<List<T>> = channel.cleanInterceptors()
    override fun cleanChannels(): Channel<List<T>> = channel.cleanChannels()

    //endregion

    /**
     * to prepare the [dataCollect]
     */
    private fun ensurePrepared() {
        for (i in channels.indices) {
            dataCollect.add(ArrayList())
        }
    }

    /**
     * check the data and detect whether to emit it.
     */
    private fun checkAndEmitToChannel() {
        var countMin = dataCollect.map { it.size }.minOrNull()
        require(countMin != null)
        while (countMin > 0) {
            val data = extractData()
            channel.emit(data)
            countMin -= 1
        }
    }


    /**
     * extract the data from [dataCollect]
     */
    private fun extractData(): List<T> {
        val data = ArrayList<T>()
        dataCollect.forEach {
            data.add(it.first())
            it.removeAt(0)
        }
        return data
    }


}