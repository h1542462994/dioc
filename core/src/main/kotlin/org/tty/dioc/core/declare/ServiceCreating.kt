package org.tty.dioc.core.declare

/**
 * a mark class for service is not created fully.
 */
data class ServiceCreating(
    val service: Any,
    val notInjectedComponents: ArrayList<ServiceProperty>
)