package org.tty.dioc.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.lifecycle.InitializeAware

@Service
class HelloService2PrintImpl: HelloService2Print {

    @Inject
    lateinit var printService1: PrintService1

    override fun printHello(): String {
        val s = "2:${printService1.print()}"
        println(s)
        return s
    }


}