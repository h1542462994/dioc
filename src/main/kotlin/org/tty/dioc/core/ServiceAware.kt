package org.tty.dioc.core

import kotlin.reflect.KClass

interface ServiceAware {
    /**
     * to get the service
     */
    fun <T: Any> getService(type: KClass<T>): T
}