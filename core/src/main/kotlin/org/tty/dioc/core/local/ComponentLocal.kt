package org.tty.dioc.core.local

import java.lang.ref.WeakReference

/**
 * the local component, used on stack visitation.
 * you should use the component local carefully, because it break the ooc.
 */
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

    /**
     * the records of the [Holder] by stack.
     */
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

    /**
     * to pop the current [Holder].
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
                val c = holder.component.get()
                require(c != null) {
                    "the component couldn't be null."
                }
                c as T
            }
            is CallerHolder -> {
                val h = holder.holder.get()
                require(h != null) {
                    "the holder couldn't be null."
                }
                holder.call.invoke(h) as T
            }
            else -> {
                throw IllegalStateException("the holder is type error.")
            }
        }
    }
}