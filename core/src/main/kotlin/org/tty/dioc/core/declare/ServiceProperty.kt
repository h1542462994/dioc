package org.tty.dioc.core.declare

import org.tty.dioc.core.error.ServiceDeclarationException

/**
 * the property of the service
 * the [ServiceProperty] is the combination of the [ServiceDeclare] and the [PropertyComponent]
 * @see [ServiceDeclare]
 * @see [PropertyComponent]
 */
class ServiceProperty(
    /**
     * the declare of the service
     */
    val serviceDeclare: ServiceDeclare,
    /**
     * the service
     */
    val service: Any,
    /**
     * the property name
     */
    val name: String,
    /**
     * the inject place
     */
    private val injectPlace: InjectPlace
) {
    /**
     * property service declare
     */
    lateinit var propertyServiceDeclare: ServiceDeclare

    /**
     * initialize the [propertyServiceDeclare] on by [serviceDeclares], then call [check] to check the structure
     */
    fun fill(serviceDeclares: ServiceDeclares) {
        propertyServiceDeclare = serviceDeclares.findByDeclare(propertyComponent.declareType)
        //check(serviceDeclares)
    }

    /**
     * the current property component.
     */
    val propertyComponent: PropertyComponent
    get() {
        return serviceDeclare.components.find { it.name == name && it.injectPlace == injectPlace }!!
    }
}