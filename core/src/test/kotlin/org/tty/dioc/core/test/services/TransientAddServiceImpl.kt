package org.tty.dioc.core.test.services

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.annotation.Component

@Component(lifecycle = Lifecycle.Transient)
class TransientAddServiceImpl: TransientAddService {
    private var value: Int = 0

    override fun add() {
        value += 1
    }

    override fun current(): Int {
        return value
    }
}