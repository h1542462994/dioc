package org.tty.dioc.observable

interface ChannelMapper<T, TR>: ChannelReceive<TR>, ChannelEmit<T>