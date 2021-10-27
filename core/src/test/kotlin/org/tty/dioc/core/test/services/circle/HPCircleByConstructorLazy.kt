package org.tty.dioc.core.test.services.circle

import org.tty.dioc.annotation.Lazy
import org.tty.dioc.annotation.Component
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.test.services.Logger

interface H2 {
    fun hello(): String
    fun print(): String
}

@Component
class H2Impl(
    private val p2: P2,
    private val logger: Logger
): H2, InitializeAware {
    override fun hello(): String {
        return "hello"
    }

    override fun print(): String {
        return p2.print()
    }

    override fun onInit() {
        logger.i("H2", "==H2Impl is created.")
    }
}

@Component
class P2(
    @Lazy private val h2: H2
) {
    fun print(): String {
        val s = "print:${h2.hello()}"
        println(s)
        return s
    }
}