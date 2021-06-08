package org.tty.dioc.core.declare.identifier

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.lifecycle.Scope

/**
 * the identifier to store the service
 */
interface ServiceIdentifier {
    companion object {
        fun ofDeclare(serviceDeclare: ServiceDeclare, scope: Scope?): ServiceIdentifier {
            return when (serviceDeclare.lifecycle) {
                Lifecycle.Singleton -> {
                    SingletonIdentifier(serviceDeclare.serviceType)
                }
                Lifecycle.Scoped -> {
                    ScopeIdentifier(serviceDeclare.serviceType, scope!!)
                }
                else -> {
                    TransientIdentifier()
                }
            }
        }
    }
}