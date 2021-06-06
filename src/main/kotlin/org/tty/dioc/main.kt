package org.tty.dioc

import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.ApplicationContextBuilder
import org.tty.dioc.core.DefaultApplicationContext
import org.tty.dioc.core.util.ClassScanner
import org.tty.dioc.service.HelloService


fun main() {
    val context = ApplicationContextBuilder()
        .usePackage("org.tty.dioc.service", true)
        .create()

    val helloService: HelloService = context.getService()

    println(helloService.hello())
}