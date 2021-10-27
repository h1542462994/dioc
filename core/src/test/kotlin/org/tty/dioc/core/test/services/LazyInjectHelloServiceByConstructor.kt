package org.tty.dioc.core.test.services

import org.tty.dioc.annotation.Lazy
import org.tty.dioc.annotation.Component

@Component
class LazyInjectHelloServiceByConstructor(@Lazy val helloService: HelloService) {

    fun lazyHello(): String {
        return helloService.hello()
    }
}