package org.tty.dioc.core.basic

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.base.FinishAware
import org.tty.dioc.core.declare.ComponentRecord
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.core.internal.ComponentStorageImpl
import org.tty.dioc.core.internal.IStorageTransaction
import org.tty.dioc.transaction.Transactional
import kotlin.reflect.KClass

/**
 * the storage for [Component]
 */
@InternalComponent
interface ComponentStorage : FinishAware, Transactional<IStorageTransaction> {
    /**
     * whether the partStorage is empty.
     */
    val isPartEmpty: Boolean

    /**
     * the first service not injected.
     */
    val partFirst: MutableMap.MutableEntry<ComponentKey, ComponentRecord>

    /**
     * find the component by [componentKey] in [ComponentStorageImpl]
     */
    fun findComponent(componentKey: ComponentKey): Any?

    /**
     * remove the component by [ComponentKey] in [ComponentStorageImpl]
     */
    fun remove(componentKey: ComponentKey)

    @Deprecated("not suggest to find component by type.")
    fun <T: Any> findComponent(type: KClass<T>): T?

    /**
     * whether exists any transaction.
     */
    fun anyTransaction(): Boolean
}