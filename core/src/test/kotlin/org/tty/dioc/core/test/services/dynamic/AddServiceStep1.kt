package org.tty.dioc.core.test.services.dynamic

class AddServiceStep1: AddService {
    var value: Int = 0

    override fun add() {
        value += 1
    }

    override fun current(): Int {
        return value
    }
}