package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Component

@Component
class PrintServiceImpl: PrintService {
    @Inject
    lateinit var helloServiceToPrint: HelloServiceToPrint

    override fun print(): String {
        val printString = "print:${helloServiceToPrint.hello()}"
        println(printString)
        return printString
    }

}