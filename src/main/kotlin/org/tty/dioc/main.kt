package org.tty.dioc

import org.tty.dioc.core.ApplicationContextBuilder
import org.tty.dioc.core.LocalApplicationContext
import org.tty.dioc.core.getService
import org.tty.dioc.service.HelloService


fun main() {
    val context = LocalApplicationContext("org.tty.dioc")

    val helloService: HelloService = context.getService()

    println(helloService.hello())
}