package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.transaction.TransactionClosedException
import org.tty.dioc.util.Transaction
import kotlin.jvm.Throws

interface IStorageTransaction: Transaction {
    /**
     * add the [serviceCreated].
     */
    @Throws(TransactionClosedException::class)
    fun addFull(serviceIdentifier: ServiceIdentifier, serviceCreated: ServiceCreated)

    /**
     * add the [serviceCreating].
     */
    @Throws(TransactionClosedException::class)
    fun addPart(serviceIdentifier: ServiceIdentifier, serviceCreating: ServiceCreating)

    /**
     * add the marking placeholder.
     */
    @Throws(TransactionClosedException::class)
    fun addEmpty(serviceDeclare: ServiceDeclare)

    /**
     * move from [ServiceCreating] to [ServiceCreated]
     */
    @Throws(TransactionClosedException::class)
    fun moveToFull(serviceIdentifier: ServiceIdentifier)

    /**
     * whether the transient component is ready.
     */
    @Throws(TransactionClosedException::class)
    fun transientNotReady(serviceDeclare: ServiceDeclare): Boolean

    /**
     * whether the component is ready.
     */
    @Throws(TransactionClosedException::class)
    fun notReady(serviceDeclare: ServiceDeclare): Boolean
}