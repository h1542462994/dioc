package org.tty.dioc.linq.extension

import org.tty.dioc.base.DebugOnly
import org.tty.dioc.linq.contract.*

@DebugOnly
fun <T> mockQueryable(): Queryable<T> {
    TODO("Not yet implemented.")
}

/**
 * to start the query and bind the [start] to query.
 */
fun <T> from(start: QueryEntry<T>): QueryFrom<T> {
    TODO("Not yet implemented.")
}

/**
 * bind the [many] to data set of the query.
 */
infix fun <T> QueryFrom<T>.of(many: Iterable<T>): QueryStart<T> {
    TODO("Not yet implemented.")
}

infix fun <T> QueryStart<T>.where(call: T.() -> Boolean): QueryPart<T> {
    TODO("Not yet implemented.")
}

infix fun <T, R> QueryPart<T>.select(call: () -> R): Queryable<R> {
    TODO("Not yet implemented.")
}

infix fun <T, R> QueryPart<T>.select(r: R): Queryable<R> {
    TODO("Not yet implemented.")
}
