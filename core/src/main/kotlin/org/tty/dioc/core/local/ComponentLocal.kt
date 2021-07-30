package org.tty.dioc.core.local

import java.lang.ref.WeakReference

/**
 * the local component, used on stack visitation.
 * you should use the component local carefully, because it break the ooc.
 */
@Suppress("UNCHECKED_CAST")
class ComponentLocal<T: Any> {
    interface Holder
    class DirectHolder(val component: WeakReference<Any>): Holder
    class CallerHolder(val holder: WeakReference<Any>, val call: (Any) -> Any): Holder

    private val records: ArrayList<Holder> = ArrayList()

    /**
     * use the direct holder to provides the current component
     */
    fun provides(component: T) {
        records.add(DirectHolder(WeakReference(component)))
    }

    /**
     * use the visitor holder to provides the current component.
     */
    fun <TH: Any> provides(holder: TH, call: (TH) -> T) {
        val c: (Any) -> Any = call as (Any) -> (Any)
        records.add(CallerHolder(WeakReference(holder), call))
    }

    /**
     * pop the current available component
     * then the component will be the previously added [ComponentLocal].
     */
    fun pop() {
        records.removeLast()
    }

    /**
     * get the current available component of [ComponentLocal]
     */
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