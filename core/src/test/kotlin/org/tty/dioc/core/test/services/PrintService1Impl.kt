package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Service

@Service
class PrintService1Impl: PrintService1 {
    @Inject
    @Lazy
    lateinit var helloService: HelloService

    @Inject
    lateinit var helloServiceNotLazy: HelloServiceNotLazy


    override fun print(): String {
        val printString = "print:${helloService.hello()}"
        println(printString)
        return printString
    }

    override fun print2(): String {
        return helloServiceNotLazy.hello()
    }
}