package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.lifecycle.InitializeAware

@Service
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