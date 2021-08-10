package org.tty.dioc.observable

object Channels {
     /**
      * to create a channel
      */
     fun <T> create(): DefaultChannel<T> {
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
      * to sync all [channel].
      * @return the channel will be emitted only all channel receive the data.
      */
     fun <T> sync(vararg channel: Channel<T>): Channel<ArrayList<T>> {
          TODO("not yet implemented.")
     }

     /**
      * to sync [channel1] and [channel2]
      * @return the channel will be emitted only all channel receive the data.
      */
     fun <T1, T2> sync(channel1: Channel<T1>, channel2: Channel<T2>): Channel<Pair<T1, T2>> {
          TODO("not yet implemented.")
     }

     /**
      * to sync [channel1], [channel2] and [channel3]
      * @return the channel will be emitted only all channel receive the data.
      */
     fun <T1, T2, T3> sync(channel1: Channel<T1>, channel2: Channel<T2>, channel3: Channel<T3>): Channel<Triple<T1, T2, T3>> {
          TODO("not yet implemented.")
     }
}