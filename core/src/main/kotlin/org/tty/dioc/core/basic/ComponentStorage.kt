package org.tty.dioc.core.basic

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.core.declare.ComponentCreating
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.base.FinishAware
import org.tty.dioc.core.storage.CombinedComponentStorage
import org.tty.dioc.transaction.Transactional
import kotlin.reflect.KClass

/**
 * the storage for [Component]
 */
@InternalComponent
interface ComponentStorage : FinishAware, Transactional<CombinedComponentStorage.CreateTransaction> {
    /**
     * whether the partStorage is empty.
     */
    val isPartEmpty: Boolean

    /**
     * the first service not injected.
     */
    val partFirst: MutableMap.MutableEntry<ComponentKey, ComponentCreating>

    /**
     * find the component by [componentKey] in [CombinedComponentStorage]
     */
    fun findComponent(componentKey: ComponentKey): Any?

    /**
     * remove the component by [ComponentKey] in [CombinedComponentStorage]
     */
    fun remove(componentKey: ComponentKey)

    fun <T: Any> findComponent(type: KClass<T>): T?

    /**
     * whether exists any transaction.
     */
    fun anyTransaction(): Boolean
}