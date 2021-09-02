package org.tty.dioc.config

import org.tty.dioc.config.keys.KeySchema

interface ConfigScope {
    operator fun <T> set(keySchema: KeySchema, value: T)
    operator fun <T> get(keySchema: KeySchema): T
}