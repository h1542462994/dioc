package org.tty.dioc.core.declare

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi
import kotlin.reflect.KClass

/**
 * serviceDeclares read.
 */
@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Readonly)
interface ReadonlyComponentDeclares: Iterable<ComponentDeclare> {

    /**
     * find in collection where [ComponentDeclare.indexTypes] contains [declarationType]
     */
    fun singleDeclarationType(declarationType: KClass<*>): ComponentDeclare

    /**
     * find in collection where [ComponentDeclare.implementationType] == [implementationType]
     */
    fun singleServiceType(implementationType: KClass<*>): ComponentDeclare

    /**
     * find in collection where [ComponentDeclare.indexTypes] contains [declarationType]
     * @return null means not match.
     */
    fun singleDeclarationTypeOrNull(declarationType: KClass<*>): ComponentDeclare?

    /**
     * to check the structure of the service on the current [componentDeclare]
     */
    fun check(componentDeclare: ComponentDeclare)
}