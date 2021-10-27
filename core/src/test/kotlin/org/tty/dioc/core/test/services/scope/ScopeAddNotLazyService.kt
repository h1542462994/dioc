package org.tty.dioc.core.test.services.scope

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.annotation.Component

@Component(lifecycle = Lifecycle.Scoped, lazy = false)
class ScopeAddNotLazyService {
    private var value = 0

    fun add() {
        value += 1
    }

    fun current(): Int {
        return value
    }
}