package org.tty.dioc.core.test.services

import org.tty.dioc.annotation.Lazy
import org.tty.dioc.annotation.Component

@Component
class LazyInjectHelloServiceByConstructor2(@Lazy helloService: HelloService) {
    init {
        println(helloService.hello())
    }
}