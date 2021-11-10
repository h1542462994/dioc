package org.tty.dioc.core.storage

import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.base.InitializeAware
import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.core.declare.ComponentRecord
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.core.key.TransientKey
import org.tty.dioc.core.launcher.ComponentKeys
import org.tty.dioc.error.TransactionClosedException
import org.tty.dioc.util.formatTable
import org.tty.dioc.util.toTruncateString
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

/**
 * the storage for service
 */
class CombinedComponentStorage: ComponentStorage {
    /**
     * the full storage, also the first level cache.
     */
    private val fullStorage = HashMap<ComponentKey, Any>()

    /**
     * the part storage, also the second level cache.
     */
    private val partStorage = HashMap<ComponentKey, ComponentRecord>()

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
         * add the [componentRecord] to [partStorage] and [marking].
         */
        @Throws(TransactionClosedException::class)
        override fun addFull(componentKey: ComponentKey, componentRecord: ComponentRecord) {
            requireNotClosed()
            val (component, componentDeclare) = componentRecord
            val entry: Any = when(componentKey) {
                is TransientKey -> {
                    WeakReference(component)
                }
                else -> {
                    component
                }
            }
            fullStorage[componentKey] = entry
            marking[componentDeclare] = entry
        }

        /**
         * add the [componentRecord] to [partStorage] and [marking]
         */
        @Throws(TransactionClosedException::class)
        override fun addPart(componentKey: ComponentKey, componentRecord: ComponentRecord) {
            requireNotClosed()
            partStorage[componentKey] = componentRecord
            marking[componentRecord.componentDeclare] = componentRecord
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
    override fun findComponent(componentKey: ComponentKey): Any? {
        return when(componentKey) {
            is TransientKey -> {
                throw IllegalArgumentException("you couldn't find transient component.")
            } else ->
                fullStorage[componentKey]
        }
    }



    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> findComponent(type: KClass<T>): T? {
        val keys = fullStorage.keys.filter {
            it.lifecycle == Lifecycle.Singleton && it.indexType == type
        }
        require(keys.isNotEmpty()) {
            return null
        }
        require(keys.size < 2) {
            "has more than one instance for provided type."
        }
        return fullStorage[keys.first()] as T
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
    override val partFirst: MutableMap.MutableEntry<ComponentKey, ComponentRecord>
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


    override fun toString(): String {

        /**
         * lifecycle | name? | type | scope?
         */
        fun split(key: ComponentKey): Array<String?> {
            return arrayOf(
                "${key.lifecycle}", key.name ?: "<anonymous>", key.indexType.qualifiedName, key.scope.toTruncateString()
            )
        }

        val realList = fullStorage.map {
            listOf(*split(it.key),
                // expect self to miss recursive kFunction call.
                if (it.value === this) {
                    ComponentKeys.componentStorage
                } else {
                    it.value.toString()
                }
            )
        }.sortedBy { it[0] }.sortedBy { it[1] }
        return formatTable("${ComponentStorage::class.simpleName}", realList, title = listOf("lifecycle", "name?", "type?", "scope?", "value")) {
            it
        }.toString()
    }

}