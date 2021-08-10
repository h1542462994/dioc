package org.tty.dioc.observable

object Channels {
     /**
      * to create a channel
      */
     fun <T> create(): DefaultChannel<T> {
          return DefaultChannel()
     }

     /**
      * to zip the channel
      */
     fun <T> zip(vararg channels: Channel<T>): Channel<T> {
          val next = DefaultChannel<T>()
          channels.forEach {
               it.next(next)
          }
          return next
     }
}