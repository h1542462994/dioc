package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * the ability to get the service
 */
interface ServiceAware {
    /**
     * to get the service by [declareType]
     */
    fun <T: Any> getService(declareType: KClass<T>): T
}