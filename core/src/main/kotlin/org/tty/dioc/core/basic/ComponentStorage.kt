package org.tty.dioc.core.basic

import org.tty.dioc.core.declare.Component
import org.tty.dioc.core.declare.InternalComponent
import org.tty.dioc.core.declare.ComponentCreating
import org.tty.dioc.core.identifier.ComponentIdentifier
import org.tty.dioc.core.lifecycle.FinishAware
import org.tty.dioc.core.storage.CombinedComponentStorage

/**
 * the storage for [Component]
 */
@InternalComponent
interface ComponentStorage : FinishAware {
    /**
     * whether the partStorage is empty.
     */
    val isPartEmpty: Boolean

    /**
     * the first service not injected.
     */
    val partFirst: MutableMap.MutableEntry<ComponentIdentifier, ComponentCreating>

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