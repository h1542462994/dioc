package org.tty.dioc.linq.contract

/**
 * represents a data set can be queried.
 * [Queryable] can be cast down to [Iterable]
 */
interface Queryable<T>: Iterable<T> {
    fun querySource(): QuerySource
    fun queryElement(): QueryElement
}