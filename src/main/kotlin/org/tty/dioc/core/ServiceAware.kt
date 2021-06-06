package org.tty.dioc.core

import org.tty.dioc.core.lifecycle.Scope

interface ServiceAware {
    /**
     * to get the service
     */
    fun <T> getService(type: Class<T>): T
    /**
     * to get service by scope
     */
    fun <T> getService(type: Class<T>, scope: Scope): T
}