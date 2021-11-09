package org.tty.dioc.util

class SimpleConsoleLogger: Logger {
    override fun v(tag: String, message: Any?) {
        println("v,${tag},${message}")
    }

    override fun d(tag: String, message: Any?) {
        println("d,${tag},${message}")
    }

    override fun i(tag: String, message: Any?) {
        println("i,${tag},${message}")
    }

    override fun w(tag: String, message: Any?) {
        println("w,${tag},${message}")
    }

    override fun e(tag: String, message: Any?) {
        println("e,${tag},${message}")
    }

    override fun e(tag: String, e: Exception) {
        e.printStackTrace()
    }
}