package org.tty.dioc.observable

import org.tty.dioc.base.*


object Channels {
     /**
      * to create a channel
      */
     fun <T> create(): ChannelFull<T> {
          return DefaultChannel()
     }

     /**
      * group the [channels] to one channel.
      * @return the channel will be emitted any channel receive the data.
      */
     fun <T> combine(vararg channels: Channel<T>): Channel<T> {
          val next = DefaultChannel<T>()
          channels.forEach {
               it.next(next)
          }
          return next
     }

     /**
      * to synchronize [channel1] and [channel2]
      * @return the channel will be emitted only all channel receive the data.
      */
     @Suppress("UNCHECKED_CAST")
     fun <T1, T2> sync(channel1: Channel<T1>, channel2: Channel<T2>): Channel<Pair<T1, T2>> {
          val channel1X = channel1.map { it as Any }
          val channel2X = channel2.map { it as Any }
          val syncChannel = SyncChannel(channels = listOf(channel1X, channel2X))
          return syncChannel.map {
               Pair(it[0] as T1, it[1] as T2)
          }
     }

     /**
      * to synchronize sync [channel1], [channel2] and [channel3]
      * @return the channel will be emitted only all channel receive the data.
      */
     @Suppress("UNCHECKED_CAST")
     fun <T1, T2, T3> sync(channel1: Channel<T1>, channel2: Channel<T2>, channel3: Channel<T3>): Channel<Triple<T1, T2, T3>> {
          val channel1X = channel1.map { it as Any }
          val channel2X = channel2.map { it as Any }
          val channel3X = channel3.map { it as Any }
          val syncChannel = SyncChannel(channels = listOf(channel1X, channel2X, channel3X))
          return syncChannel.map {
               Triple(it[0] as T1, it[1] as T2, it[2] as T3)
          }
     }

     /**
      * to synchronize all [channels].
      * @return the channel will be emitted only all channel receive the data.
      */
     fun <T> sync(vararg channels: Channel<T>): Channel<List<T>> {
          return SyncChannel(channels = channels.toList())
     }

     /**
      * to record all [channels].
      * @return the channel will be emitted by latest recorded data any channel receive the data.
      */
     fun <T> record(vararg channels: Channel<T>): InitVarargsR<T, Channel<List<T>>> {
          TODO("not yet implemented.")
     }

     /**
      * to record [channel1] and [channel2]
      * @return the channel will be emitted by latest recorded data any channel receive the data.
      */
     fun <T1, T2> record(channel1: Channel<T1>, channel2: Channel<T2>): Init2R<T1, T2, Channel<Pair<T1, T2>>> {
          TODO("not yet implemented.")
     }

     /**
      * to record [channel1], [channel2] and [channel3]
      * @return the channel will be emitted by latest recorded data any channel receive the data.
      */
     fun <T1, T2, T3> record(channel1: Channel<T1>, channel2: Channel<T2>, channel3: Channel<T3>): Init3R<T1, T2, T3, Channel<Triple<T1, T2, T3>>> {
          TODO("not yet implemented.")
     }


}