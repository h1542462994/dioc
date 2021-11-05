package org.tty.dioc.core.basic

import org.tty.dioc.base.Builder
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi
import org.tty.dioc.core.lifecycle.Scope

/**
 * factory of [Scope]
 */
@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Readonly)
interface ScopeFactory: Builder<Scope>