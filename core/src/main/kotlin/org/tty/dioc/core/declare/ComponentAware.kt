package org.tty.dioc.core.declare

import kotlin.reflect.KClass

/**
 * the ability to get the service
 */
interface ComponentAware {
    /**
     * to get the service by [declareType]
     */
    fun <T: Any> getComponent(declareType: KClass<T>): T
}