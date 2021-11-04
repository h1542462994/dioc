package org.tty.dioc.core.key

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.Lifecycle
import kotlin.reflect.KClass

/**
 * the service of the identifier which lifecycle is [Lifecycle.Transient]
 * @see [Lifecycle]
 */
@Component(lifecycle = Lifecycle.Transient)
class TransientKey: ComponentKey