package org.tty.dioc.core.basic

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.observable.channel.contract.Channel
import kotlin.reflect.KClass

/**
 * componentDeclares, also support InternalComponent look.
 */
@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Readonly)
interface ComponentDeclares: Iterable<ComponentDeclare>, ComponentDeclareAware {

    /**
     * find in collection or *internal* where [ComponentDeclare.indexTypes] contains [indexType].
     * @param indexType **index type**, index type can be found by [ComponentAware].
     */
    fun singleIndexType(indexType: KClass<*>): ComponentDeclare

    /**
     * find in collection where [ComponentDeclare.realType] == [realType].
     * @param realType **real type**.
     */
    fun singleServiceType(realType: KClass<*>): ComponentDeclare

    /**
     * find in collection or *internal* where [ComponentDeclare.indexTypes] contains [indexType].
     * @param indexType **index type**, index type can be found by [ComponentAware].
     * @return *null* means not match.
     */
    fun singleIndexTypeOrNull(indexType: KClass<*>): ComponentDeclare?

    /**
     * find in collection or *internal* where [ComponentDeclare.name] == [name].
     * @param name identify of the component.
     */
    fun singleName(name: String): ComponentDeclare

    /**
     * find in collection or *internal* where [ComponentDeclare.name] == [name].
     * @param name identify of the component.
     * @return *null* means not match.
     */
    fun singleNameOrNull(name: String): ComponentDeclare?

    /**
     * check the structure of the service on the current [componentDeclare]
     */
    fun check(componentDeclare: ComponentDeclare)

    /**
     * add componentDeclares
     */
    fun addAll(componentDeclares: List<ComponentDeclare>)

    /**
     * the channel for create a [ComponentDeclare]
     * @see Component
     */
    val createEvent: Channel<ComponentDeclare>

    /**
     * the channel for remove a [ComponentDeclare]
     */
    val removeEvent: Channel<ComponentDeclare>
}