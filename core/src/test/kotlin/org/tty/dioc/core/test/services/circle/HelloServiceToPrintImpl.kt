package org.tty.dioc.core.test.services.circle

import org.tty.dioc.annotation.Inject
import org.tty.dioc.annotation.Component

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