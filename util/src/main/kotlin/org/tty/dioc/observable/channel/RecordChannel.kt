package org.tty.dioc.observable.channel

import org.tty.dioc.base.Init2R
import org.tty.dioc.base.Init3R
import org.tty.dioc.observable.channel.contract.*

/**
 * a record channel with [channels].
 * the channel will be emitted by latest recorded data any channel receive the data.
 * a descriptor for [Channel]
 */
internal class RecordChannel<T>(private val channels: List<Channel<T>>) : RecordChannelVarargs<T> {
    private val recordMap: HashMap<Int, T> = HashMap()
    private val channel = Channels.create<List<T>>()

    init {
        require(channels.isNotEmpty())
        channels.forEachIndexed { index, channel ->
            channel.observe { data ->
                recordMap[index] = data
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

    override fun init(vararg args: T): Channel<List<T>> {
        if (args.size != channels.size) {
            throw IllegalArgumentException("args.count must be ${channels.size}")
        }
        args.forEachIndexed { index, data ->
            recordMap[index] = data
        }
        return this
    }

    /**
     * cast to [RecordChannel2] with anonymous.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2> castToRecordChannel2(): RecordChannel2<T1, T2> {
        require(channels.size == 2)
        val component1 = channel.map {
            Pair(it[0] as T1, it[1] as T2)
        }
        val component2 = object : Init2R<T1, T2, Channel<Pair<T1, T2>>> {
            override fun init(arg1: T1, arg2: T2): Channel<Pair<T1, T2>> {
                this@RecordChannel.init(arg1 as T, arg2 as T)
                return component1
            }
        }

        //region copy functions
        return object: RecordChannel2<T1, T2>,
            Channel<Pair<T1, T2>> by component1,
            Init2R<T1, T2, Channel<Pair<T1, T2>>> by component2 {}

        //endregion
    }

    /**
     * cast to [RecordChannel3] with anonymous.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3> castToRecordChannel3(): RecordChannel3<T1, T2, T3> {
        require(channels.size == 3)
        val component1 = channel.map {
            Triple(it[0] as T1, it[1] as T2, it[2] as T3)
        }
        val component2 = object : Init3R<T1, T2, T3, Channel<Triple<T1, T2, T3>>> {
            override fun init(arg1: T1, arg2: T2, arg3: T3): Channel<Triple<T1, T2, T3>> {
                this@RecordChannel.init(arg1 as T, arg2 as T, arg3 as T)
                return component1
            }
        }

        //region copy functions

        return object: RecordChannel3<T1, T2, T3>,
            Channel<Triple<T1, T2, T3>> by component1,
            Init3R<T1, T2, T3, Channel<Triple<T1, T2, T3>>> by component2 {}

        //endregion
    }

    /**
     * check the data and detect whether to emit it.
     */
    private fun checkAndEmitToChannel() {
        // prepared = true means data is ready
        var prepared = true
        for (i in channels.indices) {
            if (!recordMap.keys.contains(i)) {
                prepared = false
                break
            }
        }

        if (!prepared) {
            return
        }
        @Suppress("UNCHECKED_CAST")
        val data = channels.indices.map {
            recordMap[it] as T
        }
        channel.emit(data)
    }

}