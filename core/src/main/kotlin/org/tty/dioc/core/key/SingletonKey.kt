package org.tty.dioc.core.key

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.scope.Scope
import kotlin.reflect.KClass

/**
 * the identifier of the service which lifeCycle is [Lifecycle.Singleton]
 * @see [Lifecycle]
 */
data class SingletonKey(
    /**
     * the type of the service (runtime)
     */
    override val indexType: KClass<*>,
    override val name: String,
    override val internal: Boolean
): ComponentKey {
    override val scope: Scope?
        get() = null
    override val lifecycle: Lifecycle
        get() = Lifecycle.Singleton
}