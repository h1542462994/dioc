package org.tty.dioc.util

import java.lang.Exception

/**
 * the ability to trace the transaction.
 */
interface Transactional<T: Transaction> {
    /**
     * to begin a transaction.
     */
    fun beginTransaction(): T
}