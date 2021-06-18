package org.tty.dioc.core.local

import org.tty.dioc.core.advice.InterfaceAdvice
import org.tty.dioc.core.declare.Service

@InterfaceAdvice(serviceType = SimpleConsoleLogger::class)
@Service()
interface Logger {
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
}