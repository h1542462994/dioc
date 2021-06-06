package org.tty.dioc.core.util

import org.tty.dioc.core.declare.PropertyComponent
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.ServiceElement

/**
 * to represent the injection of the service on property
 */
data class ObjectProperty(
    val value: Any,
    val propertyComponent: PropertyComponent,
    val serviceDeclare: ServiceDeclare
)