package org.tty.dioc.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Service

@Service
class PrintService2Impl: PrintService2 {
    @Inject
    @Lazy
    lateinit var helloService1: HelloService1

    override fun print(): String {
        val s = "print2:${helloService1.hello()}"
        println(s)
        return s
    }

}