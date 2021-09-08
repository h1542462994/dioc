package org.tty.dioc.core.local

import org.tty.dioc.observable.channel.Channels
import java.lang.ref.WeakReference


@Suppress("UNCHECKED_CAST")
class DefaultComponentLocal<T: Any>: ComponentLocal<T> {

    /**
     * the holder by direct reference
     */
    class DirectHolder<T>(private val component: WeakReference<T>): ComponentLocal.Holder<T> {
        override fun isLive(): Boolean {
            return component.get() != null
        }

        override fun get(): T {
            return component.get() as T
        }
    }

    /**
     * the holder by caller reference
     */
    class CallerHolder<T>(private val holder: WeakReference<Any>, private val call: (Any) -> T):
        ComponentLocal.Holder<T> {
        override fun isLive(): Boolean {
            return holder.get() == null
        }

        override fun get(): T {
            return call.invoke(holder.get() as Any)
        }
    }

    private val records: ArrayList<ComponentLocal.Holder<T>> = ArrayList()

    override infix fun provides(component: T) {
        val h = DirectHolder(WeakReference(component))
        records.add(h)
        addChannel.emit(h)
    }

    override infix fun <TH: Any> provides(holderCall: HolderCall<TH, T>) {
        val call = holderCall.caller as (Any) -> T
        val h = CallerHolder(WeakReference(holderCall.holder), call)
        records.add(h)
        addChannel.emit(h)
    }

    override fun <TH: Any> provides(holder: TH, call: (TH) -> T) {
        val c = call as (Any) -> T
        val h = CallerHolder(WeakReference(holder), c)
        records.add(h)
        addChannel.emit(h)
    }

    override fun pop() {
        //require(records.isNotEmpty())
        val holder = records.last()
        records.removeLast()
        removeChannel.emit(holder)
    }

    override fun isEmpty(): Boolean = records.isEmpty()

    override val current: T get() {
        return currentHolder.get()
    }

    override val currentHolder: ComponentLocal.Holder<T> get()  {
        return records.last()
    }

    override val addChannel = Channels.create<ComponentLocal.Holder<T>>()

    override val removeChannel = Channels.create<ComponentLocal.Holder<T>>()

}