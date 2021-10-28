package org.tty.dioc.core.basic

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.core.declare.ComponentCreating
import org.tty.dioc.core.key.ComponentKey
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
    val partFirst: MutableMap.MutableEntry<ComponentKey, ComponentCreating>

    /**
     * find the service by [componentKey] in [CombinedComponentStorage]
     */
    fun findService(componentKey: ComponentKey): Any?
    fun remove(componentKey: ComponentKey)

    /**
     * to begin a transaction
     */
    fun beginTransaction(): CombinedComponentStorage.CreateTransaction

    /**
     * whether exists any transaction.
     */
    fun anyTransaction(): Boolean
}