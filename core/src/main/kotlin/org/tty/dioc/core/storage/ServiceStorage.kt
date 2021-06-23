package org.tty.dioc.core.storage

import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.declare.ServiceCreating
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.declare.identifier.ScopeIdentifier
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.declare.identifier.SingletonIdentifier
import org.tty.dioc.core.declare.identifier.TransientIdentifier
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

/**
 * the service Storage
 */
@Deprecated("use CombinedServiceStorage instead.")
class ServiceStorage {
    /**
     * the singleton storage
     */
    val singletonStorage = HashMap<SingletonIdentifier, Any>()

    /**
     * the scoped storage
     */
    val scopedStorage = HashMap<ScopeIdentifier, Any>()

    /**
     * the transient storage, use weak reference means the storage has linked them weakly.
     */
    val transientStorage = ArrayList<WeakReference<Any>>()

    val storage: List<Any> = singletonStorage.values
        .plus(scopedStorage.values)
        .plus(transientStorage)

    /**
     * find the available transient services by [serviceType]
     * it is not necessary if the service declaration is not changed after the initialization of the [ApplicationContext]
     */
    fun findTransientServicesByServiceType(serviceType: KClass<*>) {
        TODO("not implemented yet.")
    }

    /**
     * find the service if exists.
     * the transient service could not be identified
     * @throws IllegalArgumentException identifier not support or is [TransientIdentifier]
     */
    fun findService(identifier: ServiceIdentifier): Any? {
        return when(identifier) {
            is SingletonIdentifier -> {
                singletonStorage[identifier]
            }
            is ScopeIdentifier -> {
                scopedStorage[identifier]
            }
            is TransientIdentifier -> {
                null
            }
            else -> {
                throw IllegalArgumentException("identifier $identifier not supported")
            }
        }
    }



    /**
     * add the service by identifier
     * @throws IllegalArgumentException identifier not support.
     */
    fun addService(identifier: ServiceIdentifier, value: Any) {
       when(identifier) {
           is SingletonIdentifier -> {
               singletonStorage[identifier] = value
           }
           is ScopeIdentifier -> {
               scopedStorage[identifier] = value
           }
           is TransientIdentifier -> {
               transientStorage.add(WeakReference(value))
           }
           else -> {
               throw IllegalArgumentException("identifier $identifier not supported")
           }
       }
    }
}