package org.tty.dioc.transaction

/**
 * the ability to trace the transaction.
 */
interface Transactional<T: Transaction> {
    /**
     * to begin a transaction.
     */
    fun beginTransaction(): T

    fun <R: Any> withTransaction(action: T.() -> R): R? {
        val transaction = beginTransaction()
        try {
            val result = transaction.action()
            transaction.commit()
            return result
        } catch (e: Exception) {
            transaction.rollback()
        }
        return null
    }

    fun withTransaction(action: T.() -> Unit): Boolean {
        val transaction = beginTransaction()
        return try {
            transaction.action()
            transaction.commit()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            transaction.rollback()
            false
        }
    }
}