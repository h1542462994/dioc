package org.tty.dioc.core.declare

import org.tty.dioc.annotation.InjectPlace

/**
 * the property of the service
 * the [ComponentProperty] is the combination of the [ComponentDeclare] and the [PropertyComponent]
 * @see [ComponentDeclare]
 * @see [PropertyComponent]
 */
class ComponentProperty(
    /**
     * declare of the service
     */
    val componentDeclare: ComponentDeclare,
    /**
     * the service
     */
    val service: Any,
    /**
     * the property name
     */
    val name: String,
    /**
     * to inject place
     */
    private val injectPlace: InjectPlace
) {
    /**
     * property service declare
     */
    lateinit var propertyComponentDeclare: ComponentDeclare

    /**
     * initialize the [propertyComponentDeclare] on by [serviceDeclares], then call [check] to check the structure
     */
    fun fill(serviceDeclares: ComponentDeclares) {
        propertyComponentDeclare = serviceDeclares.singleDeclarationType(propertyComponent.declareType)
        //check(serviceDeclares)
    }

    /**
     * the current property component.
     */
    val propertyComponent: PropertyComponent
    get() {
        return componentDeclare.components.find { it.name == name && it.injectPlace == injectPlace }!!
    }
}