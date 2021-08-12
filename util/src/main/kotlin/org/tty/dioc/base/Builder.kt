package org.tty.dioc.base

/**
 * builder contract interface
 */
interface Builder<T> {
    fun create(): T
}