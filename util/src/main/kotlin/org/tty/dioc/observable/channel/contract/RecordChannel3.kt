package org.tty.dioc.observable.channel.contract

import org.tty.dioc.base.Init3R

/**
 * a record channel initializer with 3 args.
 * if you call the init, the recorded data will be initialized with init.
 * the record channel with be emitted when all recorded data is prepared.
 * @see [Channel]
 */
interface RecordChannel3<T1, T2, T3>: Channel<Triple<T1, T2, T3>>, Init3R<T1, T2, T3, Channel<Triple<T1, T2, T3>>> {
    /**
     * if you call the init, the recorded data will be initialized with init.
     * the record channel with be emitted when all recorded data is prepared.
     * @see [Channel]
     */
    override fun init(arg1: T1, arg2: T2, arg3: T3): Channel<Triple<T1, T2, T3>>
}