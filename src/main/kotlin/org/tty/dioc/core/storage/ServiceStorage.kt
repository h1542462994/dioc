package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ScopeIdentifier

/**
 * the service Storage
 */
class ServiceStorage {
    /**
     * the singleton storage
     */
    val singletonStorage = HashMap<Class<*>, Any>()

    /**
     * the scoped storage
     */
    val scopedStorage = HashMap<ScopeIdentifier, Any>()
}