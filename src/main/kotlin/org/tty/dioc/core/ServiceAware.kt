package org.tty.dioc.core

interface ServiceAware {
    /**
     * to get the service
     */
    fun <T> getService(type: Class<T>): T
}