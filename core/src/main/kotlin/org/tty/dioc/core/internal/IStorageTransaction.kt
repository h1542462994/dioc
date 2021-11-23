package org.tty.dioc.core.internal

import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.declare.ComponentRecord
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.error.TransactionClosedException
import org.tty.dioc.transaction.Transaction
import kotlin.jvm.Throws

sealed interface IStorageTransaction: Transaction {
    /**
     * add the [componentRecord].
     */
    @Throws(TransactionClosedException::class)
    fun addFull(componentKey: ComponentKey, componentRecord: ComponentRecord)

    /**
     * add the [componentRecord].
     */
    @Throws(TransactionClosedException::class)
    fun addPart(componentKey: ComponentKey, componentRecord: ComponentRecord)

    /**
     * add the marking placeholder.
     */
    @Throws(TransactionClosedException::class)
    fun addEmpty(componentDeclare: ComponentDeclare)

    /**
     * move from creating to "created".
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