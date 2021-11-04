package org.tty.dioc.core.test.services

import org.tty.dioc.annotation.Inject
import org.tty.dioc.annotation.Component
import org.tty.dioc.base.InitializeAware

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