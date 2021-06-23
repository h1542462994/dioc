package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.Service

@Service(lifecycle = Lifecycle.Transient)
class HelloServiceTSImpl: HelloServiceTS {

    @Inject
    lateinit var printService: PrintServiceTS

    override fun hello(): String {
        return "hello"
    }

    override fun print(): String {
        return printService.print()
    }

}