package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi
import org.tty.dioc.core.internal.ComponentResolverImpl

/**
 * the resolver for component. the default implementation is [ComponentResolverImpl]
 * @see ComponentResolverImpl
 */
@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Readonly)
interface ComponentResolver {
    fun <T> resolve(declare: ComponentDeclare): T
    val storage: ComponentStorage
}