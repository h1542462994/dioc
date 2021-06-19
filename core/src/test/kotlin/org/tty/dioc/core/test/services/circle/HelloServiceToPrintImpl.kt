package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Service

@Service
class HelloServiceToPrintImpl: HelloServiceToPrint {
    @Inject
    lateinit var printServiceSingleton: PrintServiceSingleton

    override fun hello(): String {
        return "hello"
    }

    override fun print(): String {
        return printServiceSingleton.print()
    }
}