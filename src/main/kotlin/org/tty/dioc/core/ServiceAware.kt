package org.tty.dioc.core

/**
 * to aware the service
 */
interface ServiceAware {
    fun <T> getService(): T
}