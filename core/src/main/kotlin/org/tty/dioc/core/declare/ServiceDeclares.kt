
package org.tty.dioc.core.declare

import kotlin.reflect.KClass

class ServiceDeclares(serviceDeclares: List<ServiceDeclare>) : Iterable<ServiceDeclare> {


    private val container = ArrayList<ServiceDeclare>()
    init {
        container.addAll(serviceDeclares)
    }

    override fun iterator(): Iterator<ServiceDeclare> {
        return container.iterator()
    }

    fun findByDeclare(declareType: KClass<*>): ServiceDeclare {
        return this.single { it.declarationTypes.contains(declareType) }
    }

    fun findByService(serviceType: KClass<*>): ServiceDeclare {
        return this.single { it.serviceType == serviceType }
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