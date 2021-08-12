package org.tty.dioc.base

interface InitVarargsR<T, R> {
    fun init(vararg args: T): R
}