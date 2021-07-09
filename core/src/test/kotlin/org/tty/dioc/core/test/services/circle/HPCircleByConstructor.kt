package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Service

/**
 * helloService to print singleton by constructor inject.
 */
@Service
class H1(private val p1: P1) {
    fun hello(): String {
        return "hello"
    }
    fun print(): String {
        return p1.print()
    }
}

@Service
class P1(private val h1: H1) {
    fun print(): String {
        val s = "print:${h1.hello()}"
        println(s)
        return s
    }
}