package org.tty.dioc.service

import org.tty.dioc.core.declare.Inject

class PrintServiceImpl: PrintService {
    @Inject
    lateinit var helloService: HelloService

    override fun print() {
        println(helloService.hello())
    }
}