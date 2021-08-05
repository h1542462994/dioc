package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.identifier.ScopeIdentifier
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.declare.identifier.SingletonIdentifier
import org.tty.dioc.core.declare.identifier.TransientIdentifier
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.transaction.TransactionClosedException
import org.tty.dioc.util.Transactional
import java.lang.ref.WeakReference

/**
 * the storage for service
 */
class CombinedServiceStorage: Transactional<CombinedServiceStorage.StorageTransaction> {
    /**
     * the full storage, also the first level cache.
     */
    private val fullStorage = HashMap<ServiceIdentifier, Any>()

    /**
     * the part storage, also the second level cache.
     */
    private val partStorage = HashMap<ServiceIdentifier, ServiceCreating>()

    private var transactionCount = 0

    /**
     * the storage transaction for creating a service.
     */
    inner class StorageTransaction: IStorageTransaction {
        /**
         * to record the resolved service in [StorageTransaction]
         */
        private val marking = HashMap<ServiceDeclare, Any>()

        /**
         * whether the transaction is closed
         */
        override var closed: Boolean = false

        @Throws(TransactionClosedException::class)
        private fun requireNotClosed() {
            if (closed) {
                throw TransactionClosedException()
            }
        }

        @Throws(TransactionClosedException::class)
        /**
         * add the [serviceCreated] to [fullStorage] and [marking]
         */
        override fun addFull(serviceIdentifier: ServiceIdentifier, serviceCreated: ServiceCreated) {
            requireNotClosed()
            val (service, serviceDeclare) = serviceCreated
            val entry: Any = when(serviceIdentifier) {
                is SingletonIdentifier -> {
                    service
                }
                is ScopeIdentifier -> {
                    service
                }
                is TransientIdentifier -> {
                    WeakReference(service)
                }
                else -> {
                    throw IllegalArgumentException("serviceIdentifier $serviceIdentifier not supported")
                }
            }
            fullStorage[serviceIdentifier] = entry
            marking[serviceDeclare] = entry
        }

        @Throws(TransactionClosedException::class)
        /**
         * add the [serviceCreating] to [partStorage] and [marking]
         */
        override fun addPart(serviceIdentifier: ServiceIdentifier, serviceCreating: ServiceCreating) {
            requireNotClosed()
            partStorage[serviceIdentifier] = serviceCreating
            marking[serviceCreating.serviceDeclare] = serviceCreating
        }

        @Throws(TransactionClosedException::class)
        /**
         * add [serviceDeclare] to [marking]
         */
        override fun addEmpty(serviceDeclare: ServiceDeclare) {
            requireNotClosed()
            marking[serviceDeclare] = Any()
        }

        @Throws(TransactionClosedException::class)
        /**
         * move the service from [partStorage] to [fullStorage]
         */
        override fun moveToFull(serviceIdentifier: ServiceIdentifier) {
            requireNotClosed()
            val creating = partStorage[serviceIdentifier]!!
            partStorage.remove(serviceIdentifier)
            fullStorage[serviceIdentifier] = creating.service
            marking[creating.serviceDeclare] = creating.service
        }

        @Throws(TransactionClosedException::class)
        /**
         * whether the transient service is not ready.
         */
        override fun transientNotReady(serviceDeclare: ServiceDeclare): Boolean {
            requireNotClosed()
            return serviceDeclare.lifecycle == Lifecycle.Transient &&
                    marking.containsKey(serviceDeclare)
        }

        @Throws(TransactionClosedException::class)
        /**
         * whether the service is created.
         */
        override fun notReady(serviceDeclare: ServiceDeclare): Boolean {
            requireNotClosed()
            return marking.containsKey(serviceDeclare)
        }

        @Throws(TransactionClosedException::class)
        /**
         * commit the changes
         */
        override fun commit() {
            requireNotClosed()
            closed = true
            transactionCount--
            marking.values.forEach {
                var entry: Any? = it
                if (it is WeakReference<*>) {
                    entry = it.get()
                }

                if (entry != null && entry is InitializeAware) {
                    entry.onInit()
                }
            }
        }

        @Throws(TransactionClosedException::class)
        override fun rollback() {
            requireNotClosed()
            closed = true
            transactionCount--
            marking.forEach { (_, v) ->
                fullStorage.entries.removeIf {
                    it.value === v
                }
                partStorage.entries.removeIf {
                    it.value === v
                }
            }
        }
    }

    /**
     * find the service by [serviceIdentifier] in [CombinedServiceStorage]
     */
    fun findService(serviceIdentifier: ServiceIdentifier): Any? {
        return when(serviceIdentifier) {
            is SingletonIdentifier -> {
                fullStorage[serviceIdentifier]
            }
            is ScopeIdentifier -> {
                fullStorage[serviceIdentifier]
            }
            is TransientIdentifier -> {
                null
            }
            else -> {
                throw IllegalArgumentException("serviceIdentifier $serviceIdentifier not supported")
            }
        }
    }

    fun findPart(identifier: ServiceIdentifier): Any? {
        return partStorage[identifier]
    }

    /**
     * whether the [partStorage] is empty.
     */
    val isPartEmpty: Boolean
    get() {
        return partStorage.isEmpty()
    }

    /**
     * the first service not injected.
     */
    val partFirst: MutableMap.MutableEntry<ServiceIdentifier, ServiceCreating>
    get() {
        return partStorage.entries.first()
    }

    /**
     * to begin a transaction
     */
    override fun beginTransaction(): StorageTransaction {
        val transaction = StorageTransaction()
        transactionCount++
        return transaction
    }

    /**
     * whether exists any transaction.
     */
    fun anyTransaction(): Boolean {
        return transactionCount != 0
    }
}