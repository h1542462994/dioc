package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.annotation.InternalComponent

/**
 * the resolver for declare of the component.
 *
 */
@InternalComponent
interface ComponentDeclareResolver {
    fun getDeclarations(): List<ComponentDeclare>
}