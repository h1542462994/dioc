package org.tty.dioc.core.declare

/**
 * the property of the service
 * the [ServiceProperty] is the combination of the [ComponentDeclare] and the [PropertyComponent]
 * @see [ComponentDeclare]
 * @see [PropertyComponent]
 */
class ServiceProperty(
    /**
     * the declare of the service
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
     * the inject place
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
    fun fill(serviceDeclares: ReadonlyServiceDeclares) {
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