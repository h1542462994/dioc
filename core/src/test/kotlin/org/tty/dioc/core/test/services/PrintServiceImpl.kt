package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Service

@Service
class PrintServiceImpl: PrintService {
    @Inject
    lateinit var helloServiceToPrint: HelloServiceToPrint

    override fun print(): String {
        val printString = "print:${helloServiceToPrint.hello()}"
        println(printString)
        return printString
    }

}