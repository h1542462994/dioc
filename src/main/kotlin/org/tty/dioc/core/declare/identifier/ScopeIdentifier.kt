package org.tty.dioc.core.declare.identifier

import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

/**
 * the identifier of the service on the scope
 */
data class ScopeIdentifier(
    /**
     * the type of service (runtime)
     */
    val serviceType: KClass<*>,
    val scope: Scope
): ServiceIdentifier