package org.tty.dioc.observable.channel.contract

/**
 * combination of [Channel] and [ChannelEmit]
 */
interface ChannelFull<T>: Channel<T>, ChannelEmit<T>