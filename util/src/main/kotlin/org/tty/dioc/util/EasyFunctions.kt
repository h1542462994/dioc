package org.tty.dioc.util

fun <T, R> T?.optional(mapper: T.() -> R): R? {
    return if(this == null) {
        null
    } else {
        mapper(this)
    }
}
