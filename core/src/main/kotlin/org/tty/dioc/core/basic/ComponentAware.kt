package org.tty.dioc.core.basic

import kotlin.reflect.KClass
import org.tty.dioc.core.ApplicationContext

/**
 * the ability to get the service
 * @see [ApplicationContext]
 */
interface ComponentAware {
    /**
     * to get the service by [declareType]
     */
    fun <T: Any> getComponent(declareType: KClass<T>): T
}