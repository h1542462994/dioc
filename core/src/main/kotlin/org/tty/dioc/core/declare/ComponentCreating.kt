package org.tty.dioc.core.declare

internal interface ComponentState

/**
 * the data class to mark a service which is creating.
 */
data class ComponentCreating(
    val service: Any,
    val componentDeclare: ComponentDeclare,
    val notInjectedComponents: ArrayList<ComponentProperty>
): ComponentState

/**
 * the data class to mark a service which is created.
 */
data class ServiceCreated(
    val service: Any,
    val componentDeclare: ComponentDeclare
): ComponentState