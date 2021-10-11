package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Component

@Component
class LazyInjectHelloServiceByConstructor(@Lazy val helloService: HelloService) {

    fun lazyHello(): String {
        return helloService.hello()
    }
}