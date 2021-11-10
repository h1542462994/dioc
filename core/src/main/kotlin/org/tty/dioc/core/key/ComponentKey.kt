package org.tty.dioc.core.key

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

/**
 * the key for component
 */
interface ComponentKey {
    val name: String?
    val scope: Scope?
    val lifecycle: Lifecycle
    val indexType: KClass<*>

    companion object {
        @Deprecated("use componentDeclare.createKey(scope) instead")
        fun ofDeclare(componentDeclare: ComponentDeclare, scope: Scope?): ComponentKey {
            return when (componentDeclare.lifecycle) {
                Lifecycle.Singleton -> {
                    SingletonKey(componentDeclare.implementationType, null)
                }
                Lifecycle.Scoped -> {
                    if (scope == null) {
                        throw ServiceConstructException("you couldn't get a scoped service out of a scope.")
                    }
                    ScopeKey(componentDeclare.implementationType, null, scope)
                }
                Lifecycle.Transient -> {
                    TransientKey(componentDeclare.implementationType, null)
                }
            }
        }
    }
}