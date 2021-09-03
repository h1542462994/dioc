package org.tty.dioc

import org.tty.dioc.config.ConfigScope

interface ConfigAware {
    fun configure(action: ConfigScope.() -> Unit)
}