package org.tty.dioc.util

import org.tty.dioc.annotation.InternalComponent

@InternalComponent
interface Logger {
    fun v(tag: String, message: Any?)
    fun d(tag: String, message: Any?)
    fun i(tag: String, message: Any?)
    fun w(tag: String, message: Any?)
    fun e(tag: String, message: Any?)
    fun e(tag: String, e: Exception)
}
