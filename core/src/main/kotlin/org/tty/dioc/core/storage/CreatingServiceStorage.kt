package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.declare.identifier.TransientIdentifier

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

    @Deprecated("you should use toReadyTransient instead.")
    fun isCreating(serviceDeclare: ServiceDeclare): Boolean {
        return container.values.any { it.serviceDeclare == serviceDeclare }
    }

    val readyTransients: List<ServiceCreating>
    get() {
        return container.entries.filter { it.key is TransientIdentifier }.map { it.value }
    }

    fun first(): MutableMap.MutableEntry<ServiceIdentifier, ServiceCreating> {
        return container.entries.first()
    }
}