package org.tty.dioc.core.test.services.dynamic

class AddServiceStep2: AddService {
    var value: Int = 0

    override fun add() {
        value += 2
    }

    override fun current(): Int {
        return value
    }
}