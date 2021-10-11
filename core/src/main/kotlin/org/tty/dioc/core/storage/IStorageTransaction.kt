package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.declare.ComponentCreating
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.identifier.ComponentIdentifier
import org.tty.dioc.transaction.TransactionClosedException
import org.tty.dioc.transaction.Transaction
import kotlin.jvm.Throws

sealed interface IStorageTransaction: Transaction {
    /**
     * add the [serviceCreated].
     */
    @Throws(TransactionClosedException::class)
    fun addFull(componentIdentifier: ComponentIdentifier, serviceCreated: ServiceCreated)

    /**
     * add the [serviceCreating].
     */
    @Throws(TransactionClosedException::class)
    fun addPart(componentIdentifier: ComponentIdentifier, serviceCreating: ComponentCreating)

    /**
     * add the marking placeholder.
     */
    @Throws(TransactionClosedException::class)
    fun addEmpty(componentDeclare: ComponentDeclare)

    /**
     * move from [ComponentCreating] to [ServiceCreated]
     */
    @Throws(TransactionClosedException::class)
    fun moveToFull(componentIdentifier: ComponentIdentifier)

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