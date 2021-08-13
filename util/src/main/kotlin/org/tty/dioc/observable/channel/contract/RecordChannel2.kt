package org.tty.dioc.observable.channel.contract

import org.tty.dioc.base.Init2R

/**
 * a record channel initializer with 2 args.
 * if you call the init, the recorded data will be initialized with init.
 * the record channel with be emitted when all recorded data is prepared.
 * @see [Channel]
 */
interface RecordChannel2<T1, T2>: Channel<Pair<T1, T2>>, Init2R<T1, T2, Channel<Pair<T1, T2>>> {
    /**
     * if you call the init, the recorded data will be initialized with init.
     * the record channel with be emitted when all recorded data is prepared.
     * @see [Channel]
     */
    override fun init(arg1: T1, arg2: T2): Channel<Pair<T1, T2>>
}