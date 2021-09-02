package org.tty.dioc.core.local

import org.tty.dioc.config.keys.ConfigKeys
import org.tty.dioc.config.keys.ProviderKeySchema
import org.tty.dioc.advice.InterfaceAdvice

private const val loggerKey = "org.tty.dioc.core.local.Logger"

@InterfaceAdvice(key = loggerKey)
interface Logger {
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
}

val ConfigKeys.logger: ProviderKeySchema
get() = config(ProviderKeySchema(loggerKey, Logger::class, SimpleConsoleLogger::class, true))

