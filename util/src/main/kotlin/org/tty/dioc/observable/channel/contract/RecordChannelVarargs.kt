package org.tty.dioc.observable.channel.contract

import org.tty.dioc.base.InitVarargsR

/**
 * a record channel initializer with vararg.
 * if you call the init, the recorded data will be initialized with init.
 * the record channel with be emitted when all recorded data is prepared.
 * @see [Channel]
 */
interface RecordChannelVarargs<T>: Channel<List<T>>, InitVarargsR<T, Channel<List<T>>> {
    /**
     * if you call the init, the recorded data will be initialized with init.
     * the record channel with be emitted when all recorded data is prepared.
     * @see [Channel]
     */
    override fun init(vararg args: T): Channel<List<T>>
}