package org.tty.dioc.observable

/**
 * combination of [Channel] and [ChannelEmit]
 */
interface ChannelFull<T>: Channel<T>, ChannelEmit<T>