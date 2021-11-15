package org.tty.dioc.core.key

import org.tty.dioc.annotation.Component
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
    override val indexType: KClass<*>,
    override val name: String,
    /**
     * the scope
     */
    override val scope: Scope,
): ComponentKey {
    override val lifecycle: Lifecycle
        get() = Lifecycle.Scoped
    override val internal: Boolean
        get() = false
}