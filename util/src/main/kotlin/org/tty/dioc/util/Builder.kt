package org.tty.dioc.util

/**
 * the generate builder
 */
interface Builder<T> {
    fun create(): T
}