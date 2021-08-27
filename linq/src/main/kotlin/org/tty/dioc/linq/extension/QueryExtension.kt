package org.tty.dioc.linq.extension

import org.tty.dioc.base.DebugOnly
import org.tty.dioc.linq.contract.*
import org.tty.dioc.reflect.virtual.Virtual

// @file::TODO("添加注释")

@DebugOnly
fun <T> mockQueryable(): Queryable<T> {
    TODO("Not yet implemented.")
}

/**
 * to start the query and bind the [entry] to query.
 * from(i)
 */
fun <T> from(entry: QueryEntry<T>): QueryFrom1<T> {
    TODO("Not yet implemented.")
}

/**
 * bind the [many] to data set of the query.
 * from(i) in collection
 */
infix fun <T> QueryFrom1<T>.of(many: Iterable<T>): QueryStart1<T> {
    TODO("Not yet implemented.")
}

/**
 * to start the second entry and bind the [entry] to query.
 * from (i) in collection from(j)
 */
infix fun <T1, T2> QueryStart1<T1>.from(entry: QueryEntry<T2>): QueryFrom2<T1, T2> {
    TODO("Not yet implemented.")
}

/**
 * bind the [many] to data set of the query.
 * from(i) in collection from (j) in collection
 */
infix fun <T1, T2> QueryFrom2<T1, T2>.of(many: Iterable<T1>): QueryStart2<T1, T2> {
    TODO("Not yet implemented.")
}

infix fun <T> QueryStart1<T>.where(statement: Virtual<Boolean>): QueryPart {
    TODO("Not yet implemented.")
}

infix fun <T1, T2> QueryStart2<T1, T2>.where(statement: Virtual<Boolean>): QueryPart {
    TODO("Not yet implemented.")
}

infix fun <R> QueryPart.select(call: () -> R): Queryable<R> {
    TODO("Not yet implemented.")
}

infix fun <R> QueryPart.select(result: Virtual<R>): Queryable<R> {
    TODO("Not yet implemented.")
}

infix fun <R1, R2> QueryPart.select(result: Pair<Virtual<R1>, Virtual<R2>>): Queryable<Pair<R1, R2>> {
    TODO("Not yet implemented.")
}

infix fun QueryPart.and(statement: Virtual<Boolean>): QueryPart {
    TODO("Not yet implemented.")
}

infix fun  QueryPart.or(statement: Virtual<Boolean>): QueryPart {
    TODO("Not yet implemented.")
}

