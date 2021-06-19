package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.lifecycle.InitializeAware

@Service(lifecycle = Lifecycle.Transient)
class PrintServiceTransientImpl: PrintServiceTransient, InitializeAware {
    @Inject
    lateinit var helloService: HelloServiceTransient

    override fun print(): String {
        val s = "print:${helloService.hello()}"
        println(s)
        return s
    }

    override fun onInit() {
        println("=== PrintService2Impl is created.")
    }

}