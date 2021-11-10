package org.tty.dioc.core.declare

data class ComponentRecord(
    val service: Any,
    val componentDeclare: ComponentDeclare,
    val notInjectedComponents: ArrayList<ComponentProperty> = arrayListOf()
) {
    val ready = notInjectedComponents.isEmpty()
}