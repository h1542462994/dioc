package org.tty.dioc.core.util

import org.tty.dioc.core.declare.PropertyComponent
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.ServiceElement

/**
 * to represent the injection of the service on property
 */
data class ObjectProperty(
    /**
     * the service to be injected
     */
    val service: Any,
    /**
     * the declare of the property
     */
    val propertyComponent: PropertyComponent,
    /**
     * the service declaration of the property
     */
    val serviceDeclare: ServiceDeclare
)