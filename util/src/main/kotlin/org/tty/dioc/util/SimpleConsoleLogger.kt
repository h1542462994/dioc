package org.tty.dioc.util

class SimpleConsoleLogger: Logger {
    override fun v(tag: String, message: String) {
        println("v,${tag},${message}")
    }

    override fun d(tag: String, message: String) {
        println("d,${tag},${message}")
    }

    override fun i(tag: String, message: String) {
        println("i,${tag},${message}")
    }

    override fun w(tag: String, message: String) {
        println("w,${tag},${message}")
    }

    override fun e(tag: String, message: String) {
        println("e,${tag},${message}")
    }
}