package org.tty.dioc.service

import org.tty.dioc.core.declare.Inject
import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.Service

@Service
class PrintServiceImpl: PrintService {
    @Inject
    lateinit var helloService: HelloService

    override fun print() {
        println(helloService.hello())
    }
}