package org.tty.dioc.core.declare.identifier

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.Scope

/**
 * the identifier to store the service
 * @see [Lifecycle]
 */
interface ServiceIdentifier {
    companion object {
        fun ofDeclare(serviceDeclare: ServiceDeclare, scope: Scope?): ServiceIdentifier {
            return when (serviceDeclare.lifecycle) {
                Lifecycle.Singleton -> {
                    SingletonIdentifier(serviceDeclare.implementationType)
                }
                Lifecycle.Scoped -> {
                    if (scope == null) {
                        throw ServiceConstructException("you couldn't get a scoped service out of a scope.")
                    }
                    ScopeIdentifier(serviceDeclare.implementationType, scope)
                }
                else -> {
                    TransientIdentifier()
                }
            }
        }
    }
}