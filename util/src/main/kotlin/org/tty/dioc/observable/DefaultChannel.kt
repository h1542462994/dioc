package org.tty.dioc.observable

/**
 * default channel implementation
 */
class DefaultChannel<T>: ChannelReceive<T>, ChannelEmit<T> {
    private val channels: ArrayList<ChannelEmit<T>> = ArrayList()
    private val receivers: ArrayList<ChannelReceiver1<T>> = ArrayList()
    private var needCreateCombinedChannelEmit = true
    private lateinit var combinedChannelEmit: CombinedChannelEmit<T>

    /**
     * add the [receiver]
     */
    override fun receive(receiver: ChannelReceiver1<T>) {
        receivers.add(receiver)
    }

    override fun <TE : Throwable> receive(receiver: ChannelReceiver2<T, TE>) {
        receivers.add(receiver)
    }

    private fun ensureCombinedChannelEmitCreated() {
        if (needCreateCombinedChannelEmit) {
            needCreateCombinedChannelEmit = false
            combinedChannelEmit = CombinedChannelEmit(channels)
        }
    }

    /**
     * get the next emit.
     */
    private fun nextEmit(index: Int): ChannelEmit<T> {
        require(index in receivers.indices)

        val nextEmit: ChannelEmit<T> =
        if (index in 1 until receivers.size){
            nextEmit(index - 1)
        } else {
            combinedChannelEmit
        }

        val emit = object: ChannelEmit<T> {
            override fun emit(data: T) {
                nextEmit.emit(data)
            }

            override fun throws(throwable: Throwable) {
                throw IllegalStateException("throw unreachable.")
            }
        }

        return emit
    }

    override fun emit(data: T) {
        ensureCombinedChannelEmitCreated()

        if (receivers.isNotEmpty()) {
            receivers.first().onSuccess(
                data, nextEmit(receivers.size - 1)
            )
        } else {
            combinedChannelEmit.emit(data)
        }
    }

    override fun throws(throwable: Throwable) {
        TODO("Not yet implemented")
    }
}