package org.tty.dioc.core.local

import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class ComponentLocal<T: Any> {
    interface Holder
    class DirectHolder(val component: WeakReference<Any>): Holder
    class CallerHolder(val holder: WeakReference<Any>, val call: (Any) -> Any): Holder

    private val records: ArrayList<Holder> = ArrayList()

    fun provides(component: T) {
        records.add(DirectHolder(WeakReference(component)))
    }
    fun <TH: Any> provides(holder: TH, call: (TH) -> T) {
        val c: (Any) -> Any = call as (Any) -> (Any)
        records.add(CallerHolder(WeakReference(holder), call))
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