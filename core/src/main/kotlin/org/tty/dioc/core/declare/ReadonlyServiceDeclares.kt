package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * serviceDeclares read.
 */
interface ReadonlyServiceDeclares: Iterable<ComponentDeclare> {

    /**
     * find in collection where [ComponentDeclare.declarationTypes] contains [declarationType]
     */
    fun singleDeclarationType(declarationType: KClass<*>): ComponentDeclare

    /**
     * find in collection where [ComponentDeclare.implementationType] == [implementationType]
     */
    fun singleServiceType(implementationType: KClass<*>): ComponentDeclare

    /**
     * find in collection where [ComponentDeclare.declarationTypes] contains [declarationType]
     * @return null means not match.
     */
    fun singleDeclarationTypeOrNull(declarationType: KClass<*>): ComponentDeclare?

    /**
     * to check the structure of the service on the current [componentDeclare]
     */
    fun check(componentDeclare: ComponentDeclare)
}