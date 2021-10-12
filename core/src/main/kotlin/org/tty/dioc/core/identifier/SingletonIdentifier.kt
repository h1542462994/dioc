package org.tty.dioc.core.identifier

import org.tty.dioc.core.declare.Lifecycle
import kotlin.reflect.KClass

/**
 * the identifier of the service which lifeCycle is [Lifecycle.Singleton]
 * @see [Lifecycle]
 */
data class SingletonIdentifier(
    /**
     * the type of the service (runtime)
     */
    val serviceType: KClass<*>
): ComponentIdentifier