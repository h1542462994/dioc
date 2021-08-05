package org.tty.dioc.util

/**
 * the ability to trace the transaction.
 */
interface Transactional<T: Transaction> {
    /**
     * to begin a transaction.
     */
    fun beginTransaction(): T
}