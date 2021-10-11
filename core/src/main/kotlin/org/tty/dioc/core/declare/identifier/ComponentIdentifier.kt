package org.tty.dioc.core.declare.identifier

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.Scope

/**
 * the identifier to store the service
 * @see [Lifecycle]
 */
interface ComponentIdentifier {
    companion object {
        fun ofDeclare(componentDeclare: ComponentDeclare, scope: Scope?): ComponentIdentifier {
            return when (componentDeclare.lifecycle) {
                Lifecycle.Singleton -> {
                    SingletonIdentifier(componentDeclare.implementationType)
                }
                Lifecycle.Scoped -> {
                    if (scope == null) {
                        throw ServiceConstructException("you couldn't get a scoped service out of a scope.")
                    }
                    ScopeIdentifier(componentDeclare.implementationType, scope)
                }
                Lifecycle.Transient -> {
                    TransientIdentifier()
                }
            }
        }
    }
}