package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.declare.ComponentCreating
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.error.TransactionClosedException
import org.tty.dioc.transaction.Transaction
import kotlin.jvm.Throws

sealed interface IStorageTransaction: Transaction {
    /**
     * add the [serviceCreated].
     */
    @Throws(TransactionClosedException::class)
    fun addFull(componentKey: ComponentKey, serviceCreated: ServiceCreated)

    /**
     * add the [serviceCreating].
     */
    @Throws(TransactionClosedException::class)
    fun addPart(componentKey: ComponentKey, serviceCreating: ComponentCreating)

    /**
     * add the marking placeholder.
     */
    @Throws(TransactionClosedException::class)
    fun addEmpty(componentDeclare: ComponentDeclare)

    /**
     * move from [ComponentCreating] to [ServiceCreated]
     */
    @Throws(TransactionClosedException::class)
    fun moveToFull(componentKey: ComponentKey)

    /**
     * whether the transient component is ready.
     */
    @Throws(TransactionClosedException::class)
    fun transientNotReady(componentDeclare: ComponentDeclare): Boolean

    /**
     * whether the component is ready.
     */
    @Throws(TransactionClosedException::class)
    fun notReady(componentDeclare: ComponentDeclare): Boolean
}