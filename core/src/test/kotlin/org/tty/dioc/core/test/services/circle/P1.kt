package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Service

@Service
class P1(private val h1: H1) {
    fun print(): String {
        val s = "print:${h1.hello()}"
        println(s)
        return s
    }
}