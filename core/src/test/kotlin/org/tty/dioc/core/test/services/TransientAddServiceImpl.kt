package org.tty.dioc.core.test.services

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.Component

@Component(lifecycle = Lifecycle.Transient)
class TransientAddServiceImpl: TransientAddService {
    var value: Int = 0

    override fun add() {
        value += 1
    }

    override fun current(): Int {
        return value
    }
}