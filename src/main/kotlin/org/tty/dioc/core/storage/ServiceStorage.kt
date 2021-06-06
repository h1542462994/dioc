package org.tty.dioc.core.storage

import org.tty.dioc.core.declare.ScopeIdentifier
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.lifecycle.LifeCycle
import org.tty.dioc.core.lifecycle.Scope
import java.lang.ref.WeakReference

/**
 * the service Storage
 */
class ServiceStorage {
    /**
     * the singleton storage
     */
    val singletonStorage = HashMap<Class<*>, Any>()

    /**
     * the scoped storage
     */
    val scopedStorage = HashMap<ScopeIdentifier, Any>()

    /**
     * the transient storage, use weak reference means the storage has linked them weakly.
     */
    val transientStorage = ArrayList<WeakReference<Any>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> findSingleton(type: Class<T>): T? {
        val instance = singletonStorage[type]
        return instance as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> findScoped(type: Class<T>, scope: Scope): T? {
        val instance = scopedStorage[ScopeIdentifier(type, scope)]
        return instance as T?
    }

    fun addService(type: ServiceDeclare, scope: Scope?, value: Any) {
        if (type.lifeCycle == LifeCycle.Singleton) {
            singletonStorage[type.serviceElement.serviceType] = value
        } else if (type.lifeCycle == LifeCycle.Scoped) {
            scopedStorage[ScopeIdentifier(type.serviceElement.serviceType, scope!!)] = value
        } else {
            transientStorage.add(WeakReference(value))
        }
    }
}