package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * serviceDeclares read.
 */
interface ReadonlyServiceDeclares: Iterable<ServiceDeclare> {
    /**
     * find in collection where [ServiceDeclare.declarationTypes] contains [declareType]
     */
    fun findByDeclare(declareType: KClass<*>): ServiceDeclare

    /**
     * find in collection where [ServiceDeclare.serviceType] == [serviceType]
     */
    fun findByService(serviceType: KClass<*>): ServiceDeclare
    /**
     * to check the structure of the service on the current [serviceDeclare]
     */
    fun check(serviceDeclare: ServiceDeclare)
}