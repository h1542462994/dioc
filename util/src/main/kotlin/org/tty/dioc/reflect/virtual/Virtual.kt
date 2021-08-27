package org.tty.dioc.reflect.virtual

import kotlin.reflect.KProperty

/**
 * a delegate to access final class.
 * used for virtual proxy.
 * because cglib couldn't proxy final class, so use [Virtual] instead.
 */
interface Virtual<T> {
    /**
     * to get [property] and return a [Virtual]
     */
    operator fun <E> get(property: KProperty<E>): Virtual<E>

    /**
     * to get property with [name] and return a [Virtual]
     */
    operator fun <E> get(name: String): Virtual<E>

    /**
     * the real object
     */
    val real: T

    /**
     * the eventSource of the virtual.
     */
    val eventSource: VirtualEventSource<T>

    /**
     * the source of the virtual. to generate a virtual object by real object.
     */
    val source: VirtualSource
}