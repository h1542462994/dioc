package org.tty.dioc.core.key

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

/**
 * the identifier of the service which lifecycle is [Lifecycle.Scoped]
 * @see [Lifecycle]
 */
data class ScopeKey(
    /**
     * the type of service (runtime)
     */
    val serviceType: KClass<*>,
    /**
     * the scope
     */
    val scope: Scope
): ComponentKey