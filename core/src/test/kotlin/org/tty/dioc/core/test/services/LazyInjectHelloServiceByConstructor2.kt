package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Service

@Service
class LazyInjectHelloServiceByConstructor2(@Lazy helloService: HelloService) {
    init {
        println(helloService.hello())
    }
}