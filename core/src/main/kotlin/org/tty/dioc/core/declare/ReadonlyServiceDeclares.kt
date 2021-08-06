package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * serviceDeclares read.
 */
interface ReadonlyServiceDeclares: Iterable<ServiceDeclare> {
    /**
     * find in collection where [ServiceDeclare.declarationTypes] contains [declareType]
     */
    fun findByDeclareType(declareType: KClass<*>): ServiceDeclare

    /**
     * find in collection where [ServiceDeclare.implementationType] == [implementationType]
     */
    fun findByServiceType(implementationType: KClass<*>): ServiceDeclare
    /**
     * to check the structure of the service on the current [serviceDeclare]
     */
    fun check(serviceDeclare: ServiceDeclare)
    fun findByDeclareTypeOrNull(declareType: KClass<*>): ServiceDeclare?
    fun findByServiceTypeOrNull(implementationType: KClass<*>): ServiceDeclare?
}