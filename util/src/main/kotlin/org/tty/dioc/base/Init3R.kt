package org.tty.dioc.base

interface Init3R<T1, T2, T3, R> {
    fun init(arg1: T1, arg2: T2, arg3: T3): R
}