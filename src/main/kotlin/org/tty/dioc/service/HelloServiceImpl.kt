package org.tty.dioc.service

import org.tty.dioc.core.declare.Service

@Service
class HelloServiceImpl: HelloService {
    override fun hello(): String {
        return "hello world!"
    }
}