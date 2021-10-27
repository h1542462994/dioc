package org.tty.dioc.core.test.services

import org.tty.dioc.annotation.Inject
import org.tty.dioc.annotation.Lazy
import org.tty.dioc.annotation.Component

@Component
class LazyInjectHelloServiceImpl: LazyInjectHelloService {
    @Inject
    @Lazy
    lateinit var helloService: HelloService

    override fun lazyHello(): String {
        return helloService.hello()
    }
}