package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.identifier.ComponentIdentifier
import org.tty.dioc.core.lifecycle.FinishAware
import org.tty.dioc.core.storage.CombinedComponentStorage

interface ComponentStorage : FinishAware {
    /**
     * whether the [partStorage] is empty.
     */
    val isPartEmpty: Boolean

    /**
     * the first service not injected.
     */
    val partFirst: MutableMap.MutableEntry<ComponentIdentifier, ServiceCreating>

    /**
     * find the service by [componentIdentifier] in [CombinedComponentStorage]
     */
    fun findService(componentIdentifier: ComponentIdentifier): Any?
    fun remove(componentIdentifier: ComponentIdentifier)

    /**
     * to begin a transaction
     */
    fun beginTransaction(): CombinedComponentStorage.CreateTransaction

    /**
     * whether exists any transaction.
     */
    fun anyTransaction(): Boolean
}