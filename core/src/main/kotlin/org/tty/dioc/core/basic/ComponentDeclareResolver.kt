package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ComponentDeclare

interface ComponentDeclareResolver {
    fun getDeclarations(): List<ComponentDeclare>
}