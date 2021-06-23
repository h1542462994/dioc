package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Service

@Service
class P2(
    @Lazy private val h2: H2
) {
    fun print(): String {
        val s = "print:${h2.hello()}"
        println(s)
        return s
    }
}