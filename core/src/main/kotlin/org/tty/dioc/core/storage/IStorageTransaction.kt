package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.transaction.TransactionClosedException
import org.tty.dioc.util.Transaction
import kotlin.jvm.Throws

interface IStorageTransaction: Transaction {
    @Throws(TransactionClosedException::class)
    fun addFull(serviceIdentifier: ServiceIdentifier, serviceCreated: ServiceCreated)
    @Throws(TransactionClosedException::class)
    fun addPart(serviceIdentifier: ServiceIdentifier, serviceCreating: ServiceCreating)
    @Throws(TransactionClosedException::class)
    fun addEmpty(serviceDeclare: ServiceDeclare)
    @Throws(TransactionClosedException::class)
    fun moveToFull(serviceIdentifier: ServiceIdentifier)
    @Throws(TransactionClosedException::class)
    fun transientNotReady(serviceDeclare: ServiceDeclare): Boolean
    @Throws(TransactionClosedException::class)
    fun notReady(serviceDeclare: ServiceDeclare): Boolean
}