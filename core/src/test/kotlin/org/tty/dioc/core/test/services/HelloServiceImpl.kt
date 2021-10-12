package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Component
import org.tty.dioc.core.lifecycle.InitializeAware

@Component
class HelloServiceImpl: HelloService, InitializeAware {
    @Inject
    lateinit var logger: Logger

    override fun hello(): String {
        return "hello"
    }

    override fun onInit() {
        logger.i("HelloService", "helloService is created.")
    }
}