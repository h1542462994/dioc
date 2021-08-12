package org.tty.dioc.base

interface Init2R<T1, T2, R> {
    fun init(arg1: T1, arg2: T2): R
}