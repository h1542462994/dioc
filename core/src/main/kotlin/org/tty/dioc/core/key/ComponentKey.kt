package org.tty.dioc.core.key

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.Scope

/**
 * the identifier to store the service
 * @see [Lifecycle]
 */
interface ComponentKey {
    companion object {
        fun ofDeclare(componentDeclare: ComponentDeclare, scope: Scope?): ComponentKey {
            return when (componentDeclare.lifecycle) {
                Lifecycle.Singleton -> {
                    SingletonKey(componentDeclare.implementationType)
                }
                Lifecycle.Scoped -> {
                    if (scope == null) {
                        throw ServiceConstructException("you couldn't get a scoped service out of a scope.")
                    }
                    ScopeKey(componentDeclare.implementationType, scope)
                }
                Lifecycle.Transient -> {
                    TransientKey()
                }
            }
        }
    }
}