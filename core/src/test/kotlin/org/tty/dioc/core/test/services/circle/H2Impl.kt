package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.test.services.Logger

@Service
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