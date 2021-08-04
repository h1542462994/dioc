package org.tty.dioc.util

interface Transaction {
    /**
     * to commit a transaction.
     */
    fun commit()

    /**
     * to rollback a transaction.
     */
    fun rollback()
}