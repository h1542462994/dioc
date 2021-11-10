package org.tty.dioc.core.key

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.lifecycle.Scope
import kotlin.reflect.KClass

/**
 * the service of the identifier which lifecycle is [Lifecycle.Transient]
 * @see [Lifecycle]
 */
@Component(lifecycle = Lifecycle.Transient)
class TransientKey(
    override val indexType: KClass<*>,
    override val name: String?,
): ComponentKey {
    override val scope: Scope?
        get() = null

    override val lifecycle: Lifecycle
        get() = Lifecycle.Transient
}