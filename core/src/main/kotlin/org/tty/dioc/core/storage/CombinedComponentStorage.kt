package org.tty.dioc.core.storage

import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.declare.ServiceCreated
import org.tty.dioc.core.declare.ComponentCreating
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.key.ScopeKey
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.core.key.SingletonKey
import org.tty.dioc.core.key.TransientKey
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.error.TransactionClosedException
import org.tty.dioc.transaction.Transactional
import java.lang.ref.WeakReference

/**
 * the storage for service
 */
class CombinedComponentStorage: Transactional<CombinedComponentStorage.CreateTransaction>, ComponentStorage {
    /**
     * the full storage, also the first level cache.
     */
    private val fullStorage = HashMap<ComponentKey, Any>()

    /**
     * the part storage, also the second level cache.
     */
    private val partStorage = HashMap<ComponentKey, ComponentCreating>()

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
        override fun addFull(componentKey: ComponentKey, serviceCreated: ServiceCreated) {
            requireNotClosed()
            val (service, serviceDeclare) = serviceCreated
            val entry: Any = when(componentKey) {
                is SingletonKey -> {
                    service
                }
                is ScopeKey -> {
                    service
                }
                is TransientKey -> {
                    WeakReference(service)
                }
                else -> {
                    throw IllegalArgumentException("identifier not support.")
                }
            }
            fullStorage[componentKey] = entry
            marking[serviceDeclare] = entry
        }

        /**
         * add the [serviceCreating] to [partStorage] and [marking]
         */
        @Throws(TransactionClosedException::class)
        override fun addPart(componentKey: ComponentKey, serviceCreating: ComponentCreating) {
            requireNotClosed()
            partStorage[componentKey] = serviceCreating
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
        override fun moveToFull(componentKey: ComponentKey) {
            requireNotClosed()
            val creating = partStorage[componentKey]!!
            partStorage.remove(componentKey)
            fullStorage[componentKey] = creating.service
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
     * find the service by [componentKey] in [CombinedComponentStorage]
     */
    override fun findService(componentKey: ComponentKey): Any? {
        return when(componentKey) {
            is SingletonKey -> {
                fullStorage[componentKey]
            }
            is ScopeKey -> {
                fullStorage[componentKey]
            }
            is TransientKey -> {
                null
            }
            else -> {
                throw IllegalArgumentException("identifier not support.")
            }
        }
    }

    override fun remove(componentKey: ComponentKey) {
        fullStorage.remove(componentKey)
        partStorage.remove(componentKey)
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
    override val partFirst: MutableMap.MutableEntry<ComponentKey, ComponentCreating>
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