package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.lifecycle.InitializeAware

@Service
class PrintService2Impl: PrintService2, InitializeAware {
    @Inject
    @Lazy
    lateinit var helloService1: HelloService1

    override fun print(): String {
        val s = "print2:${helloService1.hello()}"
        println(s)
        return s
    }

    override fun onInit() {
        println("=== PrintService2Impl is created.")
    }

}