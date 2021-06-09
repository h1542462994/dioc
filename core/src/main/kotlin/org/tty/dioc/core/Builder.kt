package org.tty.dioc.core

interface Builder<T> {
    fun create(): T
}