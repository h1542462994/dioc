package org.tty.dioc.core.test.services.circle

import org.tty.dioc.annotation.Inject
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.annotation.Component

@Component(lifecycle = Lifecycle.Transient)
class HelloServiceTransientImpl: HelloServiceTransient {
    @Inject
    lateinit var printService: PrintServiceTransient

    override fun hello(): String {
        return "hello"
    }

    override fun print(): String {
        return printService.print()
    }
}