package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi

/**
 * scanner for [ComponentDeclare]
 * only support for annotation [Component]
 */
@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.CodeReadOnly)
interface ComponentDeclareScanner {
    /**
     * scan with [packageOption]
     */
    fun scan(packageOption: PackageOption): List<ComponentDeclare>

    /**
     * scan with [packageOptions]
     */
    fun scanAll(packageOptions: List<PackageOption>): List<ComponentDeclare>
}