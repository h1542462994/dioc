package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Component

@Component
class HelloServiceToPrintImpl: HelloServiceToPrint {
    @Inject
    lateinit var printService: PrintService

    override fun hello(): String {
        return "hello"
    }

    override fun print(): String {
        return printService.print()
    }
}