package org.tty.dioc.core.test.services.circle

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Service

@Service
class PrintServiceSingletonImpl: PrintServiceSingleton {
    @Inject
    lateinit var helloServiceToPrint: HelloServiceToPrint

    override fun print(): String {
        val string = "print:${helloServiceToPrint.hello()}"
        println(string)
        return string
    }
}