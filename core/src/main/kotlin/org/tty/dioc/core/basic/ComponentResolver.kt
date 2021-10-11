package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ComponentDeclare

interface ComponentResolver {
    fun <T> resolve(declare: ComponentDeclare): T
    val storage: ComponentStorage
}