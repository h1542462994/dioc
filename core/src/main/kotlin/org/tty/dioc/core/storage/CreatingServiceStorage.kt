package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.identifier.ServiceIdentifier

class CreatingServiceStorage {
    private val container = HashMap<ServiceIdentifier, ServiceCreating>()

    fun add(identifier: ServiceIdentifier, serviceCreating: ServiceCreating) {
        container[identifier] = serviceCreating
    }

    fun isEmpty(): Boolean {
        return container.isEmpty()
    }

    fun remove(identifier: ServiceIdentifier) {
        container.remove(identifier)
    }

    fun find(identifier: ServiceIdentifier): ServiceCreating? {
        return container[identifier]
    }
}