package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Service

@Service
class HelloService1Impl: HelloService1 {
    override fun hello(): String {
        return "hello"
    }
}