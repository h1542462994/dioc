package org.tty.dioc.core.basic

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.base.InitializeAware
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi

@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Declare)
interface EntryPointLoader: InitializeAware