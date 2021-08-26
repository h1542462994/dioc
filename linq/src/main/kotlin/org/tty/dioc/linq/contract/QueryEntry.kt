package org.tty.dioc.linq.contract


import  org.tty.dioc.linq.Linq
import org.tty.dioc.linq.extension.*
import org.tty.dioc.reflect.virtual.Virtual
import kotlin.reflect.KProperty

/**
 * the entry of the query, you should create with [Linq.start] and query with [from]
 * @sample {
 *
 * }
 * @see [Linq.start]
 */
interface QueryEntry<T>: Virtual<T> {
}