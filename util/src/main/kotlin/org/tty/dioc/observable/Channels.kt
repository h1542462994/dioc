package org.tty.dioc.observable


object Channels {
     /**
      * to create a channel
      */
     fun <T> create(): ChannelFull<T> {
          return DefaultChannel()
     }

     /**
      * to group the channel to one channel
      */
     fun <T> combine(vararg channels: Channel<T>): Channel<T> {
          val next = DefaultChannel<T>()
          channels.forEach {
               it.next(next)
          }
          return next
     }

     /**
      * to sync [channel1] and [channel2]
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
      * to sync [channel1], [channel2] and [channel3]
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
      * to sync all [channels].
      * @return the channel will be emitted only all channel receive the data.
      */
     fun <T> sync(vararg channels: Channel<T>): Channel<ArrayList<T>> {
          return SyncChannel(channels = channels.toList())
     }

}