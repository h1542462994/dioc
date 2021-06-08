package org.tty.dioc.core.declare

import org.tty.dioc.core.declare.ServiceDeclare.Companion.findByDeclare
import org.tty.dioc.core.error.ServiceDeclarationException

/**
 * the element of the service
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
     * to fill the service property
     */
    fun fill(serviceDeclares: List<ServiceDeclare>) {
        propertyServiceDeclare = serviceDeclares.findByDeclare(injectComponent.declareType)
        check(serviceDeclares)
    }

    fun check(serviceDeclares: List<ServiceDeclare>) {
        if (propertyServiceDeclare.lifecycle == Lifecycle.Transient && !propertyServiceDeclare.isLazyService) {
            throw ServiceDeclarationException("the transient service must is a lazy service.")
        } else {
            serviceDeclare.components.forEach {
                val aDeclare = serviceDeclares.findByDeclare(it.declareType)
                if (aDeclare.lifecycle == Lifecycle.Scoped && !it.injectLazy) {
                    throw ServiceDeclarationException("you must inject a scoped service by @Lazy")
                }
            }
        }
    }

    /**
     * the inject Component
     */
    val injectComponent: PropertyComponent
    get() {
        return serviceDeclare.components.find { it.name == name && it.injectPlace == injectPlace }!!
    }
}