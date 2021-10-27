package org.tty.dioc.core.test.services

import org.tty.dioc.annotation.Component
import org.tty.dioc.core.test.model.LogLevel
import org.tty.dioc.core.test.model.LogToken

@Component
class SeqLogger: Iterable<LogToken>, Logger {
    private val container: ArrayList<LogToken> = ArrayList()

    override fun iterator(): Iterator<LogToken> {
        return container.iterator()
    }

    private fun log(level: LogLevel, tag: String, message: String) {
        val token = LogToken(level, tag, message)
        println("[${level.level}:${tag}],${message}")
        container.add(token)
    }

    override fun top(): LogToken? {
        return container.lastOrNull()
    }

    override fun v(tag: String, message: String) {
        log(LogLevel.Verbose, tag, message)
    }

    override fun d(tag: String, message: String) {
        log(LogLevel.Debug, tag, message)
    }

    override fun i(tag: String, message: String) {
        log(LogLevel.Info, tag, message)
    }

    override fun w(tag: String, message: String) {
        log(LogLevel.Warning, tag, message)
    }

    override fun e(tag: String, message: String) {
        log(LogLevel.Error, tag, message)
    }
}