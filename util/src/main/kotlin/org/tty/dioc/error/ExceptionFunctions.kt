package org.tty.dioc.error

fun notProvided(): Nothing = throw NotProvidedException()

fun notProvided(value: String): Nothing = throw NotProvidedException("")

fun unSupported(): Nothing = throw UnsupportedOperationException("not supported.")

fun unSupported(value: String): Nothing = throw UnsupportedOperationException(value)