package org.tty.dioc.core

import kotlin.reflect.KClass

interface ServiceAware {
    /**
     * to get the service by [declareType]
     */
    fun <T: Any> getService(declareType: KClass<T>): T
}