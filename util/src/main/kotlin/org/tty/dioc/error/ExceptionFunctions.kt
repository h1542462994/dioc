package org.tty.dioc.error

fun notProvided(): Nothing = throw NotProvidedException()

fun notProvided(value: String): Nothing = throw NotProvidedException(value)

fun unSupported(): Nothing = throw UnsupportedOperationException("unSupported.")

fun unSupported(value: String): Nothing = throw UnsupportedOperationException(value)