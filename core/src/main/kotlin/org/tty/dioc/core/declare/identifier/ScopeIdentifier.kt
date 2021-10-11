package org.tty.dioc.core.declare.identifier

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

/**
 * the identifier of the service which lifecycle is [Lifecycle.Scoped]
 * @see [Lifecycle]
 */
data class ScopeIdentifier(
    /**
     * the type of service (runtime)
     */
    val serviceType: KClass<*>,
    /**
     * the scope
     */
    val scope: Scope
): ComponentIdentifier