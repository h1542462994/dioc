package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Service
import org.tty.dioc.core.lifecycle.InitializeAware

@Service(lazy = false)
class HelloServiceNotLazyImpl: HelloServiceNotLazy, InitializeAware {

    @Inject
    lateinit var logger: Logger

    override fun hello(): String {
        return "hello"
    }

    override fun onInit() {
        logger.i("HelloServiceNotLazy", "helloServiceNotLazy is created.")
    }


}