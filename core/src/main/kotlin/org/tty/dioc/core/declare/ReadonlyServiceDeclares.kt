package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * serviceDeclares read.
 */
interface ReadonlyServiceDeclares: Iterable<ServiceDeclare> {

    /**
     * find in collection where [ServiceDeclare.declarationTypes] contains [declarationType]
     */
    fun singleDeclarationType(declarationType: KClass<*>): ServiceDeclare

    /**
     * find in collection where [ServiceDeclare.implementationType] == [implementationType]
     */
    fun singleServiceType(implementationType: KClass<*>): ServiceDeclare

    /**
     * find in collection where [ServiceDeclare.declarationTypes] contains [declarationType]
     * @return null means not match.
     */
    fun singleDeclarationTypeOrNull(declarationType: KClass<*>): ServiceDeclare?

    /**
     * to check the structure of the service on the current [serviceDeclare]
     */
    fun check(serviceDeclare: ServiceDeclare)
}