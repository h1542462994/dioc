package org.tty.dioc.transaction

import org.tty.dioc.error.TransactionClosedException
import kotlin.jvm.Throws

interface Transaction {
    /**
     * to commit a transaction.
     */
    @Throws(TransactionClosedException::class)
    fun commit()

    /**
     * to roll back a transaction.
     */
    @Throws(TransactionClosedException::class)
    fun rollback()

    /**
     * whether the transaction is closed.
     */
    val closed: Boolean
}