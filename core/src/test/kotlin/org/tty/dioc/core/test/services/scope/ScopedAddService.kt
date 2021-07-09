package org.tty.dioc.core.test.services.scope

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.Service

@Service(lifecycle = Lifecycle.Scoped)
class ScopedAddService {
    private var value = 0

    fun add() {
        value += 1
    }

    fun current(): Int {
        return value
    }
}