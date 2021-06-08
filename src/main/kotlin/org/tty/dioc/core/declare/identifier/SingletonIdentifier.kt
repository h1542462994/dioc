package org.tty.dioc.core.declare.identifier

import kotlin.reflect.KClass

/**
 * the identifier of the service on the singleton
 */
data class SingletonIdentifier(
    val serviceType: KClass<*>
): ServiceIdentifier