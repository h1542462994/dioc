package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Service

@Service
class LazyInjectHelloServiceImpl: LazyInjectHelloService {

    @Inject
    @Lazy
    lateinit var helloService: HelloService

    override fun lazyHello(): String {
        return helloService.hello()
    }
}