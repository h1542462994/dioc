package org.tty.dioc.core.storage

import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.declare.ComponentCreating
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.identifier.ScopeIdentifier
import org.tty.dioc.core.identifier.ComponentIdentifier
import org.tty.dioc.core.identifier.SingletonIdentifier
import org.tty.dioc.core.identifier.TransientIdentifier
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.transaction.TransactionClosedException
import org.tty.dioc.transaction.Transactional
import java.lang.ref.WeakReference

/**
 * the storage for service
 */
class CombinedComponentStorage: Transactional<CombinedComponentStorage.CreateTransaction>, ComponentStorage {
    /**
     * the full storage, also the first level cache.
     */
    private val fullStorage = HashMap<ComponentIdentifier, Any>()

    /**
     * the part storage, also the second level cache.
     */
    private val partStorage = HashMap<ComponentIdentifier, ComponentCreating>()

    private var transactionCount = 0

    /**
     * the storage transaction for creating a service.
     */
    inner class CreateTransaction: IStorageTransaction {
        /**
         * to record the resolved service in [CreateTransaction]
         */
        private val marking = HashMap<ComponentDeclare, Any>()

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

        /**
         * add the [serviceCreated] to [partStorage] and [marking].
         */
        @Throws(TransactionClosedException::class)
        override fun addFull(componentIdentifier: ComponentIdentifier, serviceCreated: ServiceCreated) {
            requireNotClosed()
            val (service, serviceDeclare) = serviceCreated
            val entry: Any = when(componentIdentifier) {
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
                    throw IllegalArgumentException("identifier not support.")
                }
            }
            fullStorage[componentIdentifier] = entry
            marking[serviceDeclare] = entry
        }

        /**
         * add the [serviceCreating] to [partStorage] and [marking]
         */
        @Throws(TransactionClosedException::class)
        override fun addPart(componentIdentifier: ComponentIdentifier, serviceCreating: ComponentCreating) {
            requireNotClosed()
            partStorage[componentIdentifier] = serviceCreating
            marking[serviceCreating.componentDeclare] = serviceCreating
        }

        /**
         * add [componentDeclare] to [marking]
         */
        @Throws(TransactionClosedException::class)
        override fun addEmpty(componentDeclare: ComponentDeclare) {
            requireNotClosed()
            marking[componentDeclare] = Any()
        }

        /**
         * move the service from [partStorage] to [fullStorage]
         */
        @Throws(TransactionClosedException::class)
        override fun moveToFull(componentIdentifier: ComponentIdentifier) {
            requireNotClosed()
            val creating = partStorage[componentIdentifier]!!
            partStorage.remove(componentIdentifier)
            fullStorage[componentIdentifier] = creating.service
            marking[creating.componentDeclare] = creating.service
        }

        /**
         * whether the transient service is not ready.
         */
        @Throws(TransactionClosedException::class)
        override fun transientNotReady(componentDeclare: ComponentDeclare): Boolean {
            requireNotClosed()
            return componentDeclare.lifecycle == Lifecycle.Transient &&
                    marking.containsKey(componentDeclare)
        }

        /**
         * whether the service is created.
         */
        @Throws(TransactionClosedException::class)
        override fun notReady(componentDeclare: ComponentDeclare): Boolean {
            requireNotClosed()
            return marking.containsKey(componentDeclare)
        }

        /**
         * commit the changes
         */
        @Throws(TransactionClosedException::class)
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
     * find the service by [componentIdentifier] in [CombinedComponentStorage]
     */
    override fun findService(componentIdentifier: ComponentIdentifier): Any? {
        return when(componentIdentifier) {
            is SingletonIdentifier -> {
                fullStorage[componentIdentifier]
            }
            is ScopeIdentifier -> {
                fullStorage[componentIdentifier]
            }
            is TransientIdentifier -> {
                null
            }
            else -> {
                throw IllegalArgumentException("identifier not support.")
            }
        }
    }

    override fun remove(componentIdentifier: ComponentIdentifier) {
        fullStorage.remove(componentIdentifier)
        partStorage.remove(componentIdentifier)
    }

    /**
     * whether the [partStorage] is empty.
     */
    override val isPartEmpty: Boolean
    get() {
        return partStorage.isEmpty()
    }

    /**
     * the first service not injected.
     */
    override val partFirst: MutableMap.MutableEntry<ComponentIdentifier, ComponentCreating>
    get() {
        return partStorage.entries.first()
    }

    /**
     * to begin a transaction
     */
    override fun beginTransaction(): CreateTransaction {
        val transaction = CreateTransaction()
        transactionCount++
        return transaction
    }

    /**
     * whether exists any transaction.
     */
    override fun anyTransaction(): Boolean {
        return transactionCount != 0
    }

    /**
     * finish the storage.
     */
    override fun onFinish() {

    }
}