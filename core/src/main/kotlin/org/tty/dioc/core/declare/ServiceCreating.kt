package org.tty.dioc.core.declare

/**
 * the data class to mark a service which is creating.
 */
data class ServiceCreating(
    val service: Any,
    val serviceDeclare: ServiceDeclare,
    val notInjectedComponents: ArrayList<ServiceProperty>
)

/**
 * the data class to mark a service which is created.
 */
data class ServiceCreated(
    val service: Any,
    val serviceDeclare: ServiceDeclare
)