package org.tty.dioc.core.test.model

data class LogToken(
    val levelString: String,
    val tag: String,
    val message: String
){
    constructor(level: LogLevel, tag: String, message: String) : this(level.level, tag, message)

    val level: LogLevel
    get() {
        return stringToLevel(levelString)
    }

    companion object {
        fun stringToLevel(levelString: String): LogLevel {
            return LogLevel.values().single { it.level == levelString }
        }
    }
}