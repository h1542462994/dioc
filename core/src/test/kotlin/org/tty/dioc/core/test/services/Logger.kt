package org.tty.dioc.core.test.services

import org.tty.dioc.core.test.model.LogToken

/**
 * the queue logger
 */
interface Logger {
    fun top(): LogToken?
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
}