package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.lifecycle.FinishAware

interface ServiceStorage : FinishAware {
    /**
     * whether the [partStorage] is empty.
     */
    val isPartEmpty: Boolean

    /**
     * the first service not injected.
     */
    val partFirst: MutableMap.MutableEntry<ServiceIdentifier, ServiceCreating>

    /**
     * find the service by [serviceIdentifier] in [CombinedServiceStorage]
     */
    fun findService(serviceIdentifier: ServiceIdentifier): Any?
    fun remove(serviceIdentifier: ServiceIdentifier)

    /**
     * to begin a transaction
     */
    fun beginTransaction(): CombinedServiceStorage.CreateTransaction

    /**
     * whether exists any transaction.
     */
    fun anyTransaction(): Boolean
}