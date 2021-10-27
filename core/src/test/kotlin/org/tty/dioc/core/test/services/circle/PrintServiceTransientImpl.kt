package org.tty.dioc.core.test.services.circle

import org.tty.dioc.annotation.Inject
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.annotation.Component
import org.tty.dioc.core.lifecycle.InitializeAware

@Component(lifecycle = Lifecycle.Transient)
class PrintServiceTransientImpl: PrintServiceTransient, InitializeAware {
    @Inject
    lateinit var helloService: HelloServiceTransient

    override fun print(): String {
        val s = "print:${helloService.hello()}"
        println(s)
        return s
    }

    override fun onInit() {
        println("=== PrintServiceTransient is created.")
    }

}