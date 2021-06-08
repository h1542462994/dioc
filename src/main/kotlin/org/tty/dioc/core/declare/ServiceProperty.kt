package org.tty.dioc.core.declare

import org.tty.dioc.core.declare.ServiceDeclare.Companion.findByDeclare

/**
 * the element of the service
 */
class ServiceProperty(
    /**
     * the declare of the service
     */
    private val serviceDeclare: ServiceDeclare,
    /**
     * the service
     */
    val service: Any,
    /**
     * the property name
     */
    val name: String
) {
    /**
     * the inject Component
     */
    val injectComponent: PropertyComponent
    get() {
        return serviceDeclare.components.find { it.name == name && it.injectPlace == InjectPlace.InjectProperty }!!
    }
}