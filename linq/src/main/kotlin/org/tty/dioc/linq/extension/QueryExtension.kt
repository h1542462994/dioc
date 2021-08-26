package org.tty.dioc.linq.extension

import org.tty.dioc.base.DebugOnly
import org.tty.dioc.linq.contract.*
import org.tty.dioc.reflect.virtual.Virtual

@DebugOnly
fun <T> mockQueryable(): Queryable<T> {
    TODO("Not yet implemented.")
}

/**
 * to start the query and bind the [entry] to query.
 */
fun <T> from(entry: QueryEntry<T>): QueryFrom1<T> {
    TODO("Not yet implemented.")
}

infix fun <T1, T2> QueryStart1<T1>.from(entry: QueryEntry<T2>): QueryFrom2<T1, T2> {
    TODO("Not yet implemented.")
}

/**
 * bind the [many] to data set of the query.
 */
infix fun <T> QueryFrom1<T>.of(many: Iterable<T>): QueryStart1<T> {
    TODO("Not yet implemented.")
}

infix fun <T1, T2> QueryFrom2<T1, T2>.of(many: Iterable<T1>): QueryStart2<T1, T2> {
    TODO("Not yet implemented.")
}

infix fun <T> QueryStart1<T>.where(call: T.() -> Boolean): QueryPart<T> {
    TODO("Not yet implemented.")
}

infix fun <T, R> QueryPart<T>.select(call: () -> R): Queryable<R> {
    TODO("Not yet implemented.")
}

infix fun <T, R> QueryPart<T>.select(r: Virtual<R>): Queryable<R> {
    TODO("Not yet implemented.")
}
