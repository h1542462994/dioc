package org.tty.dioc.core

import org.tty.dioc.ConfigAware

interface Application {
    fun configure(config: ConfigAware)
}