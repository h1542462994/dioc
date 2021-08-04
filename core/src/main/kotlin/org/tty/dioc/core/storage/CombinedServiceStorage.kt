package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.Lifecycle
import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.identifier.ScopeIdentifier
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.declare.identifier.SingletonIdentifier
import org.tty.dioc.core.declare.identifier.TransientIdentifier
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.util.Transaction
import java.lang.ref.WeakReference

/**
 * the storage for service
 */
class CombinedServiceStorage {
    /**
     * the full storage, also the first level cache.
     */
    private val fullStorage = HashMap<ServiceIdentifier, Any>()

    /**
     * the part storage, also the second level cache.
     */
    private val partStorage = HashMap<ServiceIdentifier, ServiceCreating>()

    /**
     *
     */
    inner class StorageTransaction: Transaction {
        override fun commit() {
            TODO("Not yet implemented")
        }

        override fun rollback() {
            TODO("Not yet implemented")
        }
    }

    /**
     * the marking service, to mark the service in creating handle
     */
    private val marking = HashMap<ServiceDeclare, Any>()



    /**
     * if [isCreatingService] is true, means storage is in transaction
     */
    var isCreatingService = false
    private set

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
     * add the [service] to [fullStorage]
     */
    fun addFull(serviceIdentifier: ServiceIdentifier, serviceDeclare: ServiceDeclare, service: Any) {
        require(isCreatingService) {
            "you can only modify the storage in transaction."
        }
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

    /**
     * add the [serviceCreating] to [partStorage]
     */
    fun addPart(serviceIdentifier: ServiceIdentifier, serviceCreating: ServiceCreating) {
        partStorage[serviceIdentifier] = serviceCreating
        marking[serviceCreating.serviceDeclare] = serviceCreating
    }

    /**
     * add [serviceDeclare] to [marking]
     */
    fun addEmpty(serviceDeclare: ServiceDeclare) {
        marking[serviceDeclare] = Any()
    }

    /**
     * move the service from [partStorage] to [fullStorage]
     */
    fun moveToFull(serviceIdentifier: ServiceIdentifier) {
        val creating = partStorage[serviceIdentifier]!!
        partStorage.remove(serviceIdentifier)
        fullStorage[serviceIdentifier] = creating.service
        marking[creating.serviceDeclare] = creating.service
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
     * whether the transient service is not ready.
     */
    fun transientNotReady(serviceDeclare: ServiceDeclare): Boolean {
        return serviceDeclare.lifecycle == Lifecycle.Transient &&
                marking.containsKey(serviceDeclare)
    }

    /**
     * whether the service is created.
     */
    fun notReady(serviceDeclare: ServiceDeclare): Boolean {
        return marking.containsKey(serviceDeclare)
    }

    fun begin() {
        isCreatingService = true
    }


    /**
     * to clean the [marking]
     */
    fun commit() {
        marking.values.forEach {
            var entry: Any? = it
            if (it is WeakReference<*>) {
                entry = it.get()
            }

            if (entry != null && entry is InitializeAware) {
                entry.onInit()
            }
        }
        marking.clear()
        isCreatingService = false
    }



    /**
     * rollback the [marking]
     */
    fun rollback() {
        marking.forEach { (_, v) ->
            fullStorage.entries.removeIf {
                it.value === v
            }
            partStorage.entries.removeIf {
                it.value === v
            }
        }
        marking.clear()
        isCreatingService = false
    }

}