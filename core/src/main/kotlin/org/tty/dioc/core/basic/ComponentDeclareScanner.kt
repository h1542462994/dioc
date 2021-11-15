package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.declare.PackageOption
import org.tty.dioc.annotation.Component

/**
 * scanner for [ComponentDeclare]
 * only support for annotation [Component]
 */
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