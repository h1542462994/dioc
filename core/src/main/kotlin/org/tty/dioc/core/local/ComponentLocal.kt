package org.tty.dioc.core.local

import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class ComponentLocal<T: Any> {
    interface Holder

    /**
     * the holder by direct reference
     */
    class DirectHolder(val component: WeakReference<Any>): Holder

    /**
     * the holder by caller reference
     */
    class CallerHolder(val holder: WeakReference<Any>, val call: (Any) -> Any): Holder

    private val records: ArrayList<Holder> = ArrayList()

    /**
     * provides the [ComponentLocal] with [component]
     */
    infix fun provides(component: T) {
        records.add(DirectHolder(WeakReference(component)))
    }

    /**
     * provides the [ComponentLocal] with [holderCall]
     * @see [HolderCall]
     */
    infix fun <TH: Any> provides(holderCall: HolderCall<TH, T>) {
        val call = holderCall as (Any) -> Any
        records.add(CallerHolder(WeakReference(holderCall.holder), call))
    }

    fun pop() {
        records.removeLast()
    }

    fun current(): T {
        return when (val holder = records.last()) {
            is DirectHolder -> {
                holder.component.get()!! as T
            }
            is CallerHolder -> {
                val h = holder.holder.get()!!
                holder.call.invoke(h) as T
            }
            else -> {
                throw IllegalStateException("holder error.")
            }
        }
    }
}