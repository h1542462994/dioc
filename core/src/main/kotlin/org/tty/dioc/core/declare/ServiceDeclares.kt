
package org.tty.dioc.core.declare

import org.tty.dioc.core.error.ServiceDeclarationException
import kotlin.reflect.KClass

class ServiceDeclares(serviceDeclares: List<ServiceDeclare>) : Iterable<ServiceDeclare> {


    private val container = ArrayList<ServiceDeclare>()
    init {
        container.addAll(serviceDeclares)
    }

    override fun iterator(): Iterator<ServiceDeclare> {
        return container.iterator()
    }

    fun findByDeclareOrNull(declareType: KClass<*>): ServiceDeclare? {
        return this.singleOrNull { it.declarationTypes.contains(declareType) }
    }

    fun findByDeclare(declareType: KClass<*>): ServiceDeclare {
        return this.single { it.declarationTypes.contains(declareType) }
    }

    fun findByService(serviceType: KClass<*>): ServiceDeclare {
        return this.single { it.serviceType == serviceType }
    }

    /**
     * to check the structure of the service on the current [serviceDeclare]
     */
    fun check(serviceDeclare: ServiceDeclare) {
        if (serviceDeclare.lifecycle == Lifecycle.Transient && !serviceDeclare.isLazyService) {
            throw ServiceDeclarationException("the transient service must be a lazy service.")
        } else {
            serviceDeclare.components.forEach {
                val aDeclare = this.findByDeclareOrNull(it.declareType)
                if (aDeclare != null && aDeclare.lifecycle == Lifecycle.Scoped && !it.injectLazy) {
                    throw ServiceDeclarationException("you must inject a scoped service by @Lazy")
                }
            }
        }
    }

}

/**
 * find in collection where [ServiceDeclare.declarationTypes] contains [declareType]
 */
fun List<ServiceDeclare>.findByDeclare(declareType: KClass<*>): ServiceDeclare {
    return this.single { it.declarationTypes.contains(declareType) }
}

/**
 * find in collection where [ServiceDeclare.serviceType] == [serviceType]
 */
fun List<ServiceDeclare>.findByService(serviceType: KClass<*>): ServiceDeclare {
    return this.single { it.serviceType == serviceType }
}