package org.tty.dioc.util

fun <T1, T2> pair(arg1: T1, arg2: T2): Pair<T1, T2> {
    return Pair(arg1, arg2)
}

fun <T, R> T?.optional(mapper: T.() -> R): R? {
    return if(this == null) {
        null
    } else {
        mapper(this)
    }
}