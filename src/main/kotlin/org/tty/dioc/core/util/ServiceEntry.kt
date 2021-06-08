package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.declare.ServiceDeclare.Companion.findByDeclare
import org.tty.dioc.core.declare.ServiceDeclare.Companion.findByService
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAware
import org.tty.dioc.core.lifecycle.ServiceProxyFactory
import org.tty.dioc.core.storage.ServiceStorage
import kotlin.reflect.jvm.javaConstructor
import kotlin.reflect.jvm.jvmErasure

/**
 * the entry for create or get the service
 */
class ServiceEntry<T>(
    private val storage: ServiceStorage,
    private val serviceDeclarations: List<ServiceDeclare>,
    private val type: ServiceDeclare,
    private val scopeAware: ScopeAware
) {


    // entry function for createService
    fun getOrCreateService(): T {
        return getOrCreateService(type, scopeAware.currentScope()) as T
    }

    // the object ready to injected
    private var readyToInjects = ArrayList<ServiceProperty>()


    /**
     * the implementation of the creation of the service.
     */
    private fun getOrCreateService(serviceDeclare: ServiceDeclare, scope: Scope?): Any  {
        // first return the provided service if exists.
        val s = storage.findService(ServiceIdentifier.ofDeclare(serviceDeclare, scope))
        if (s != null) {
            return s
        }

        // then create the stub
        val stub = createStub(serviceDeclare, scope)

        // then to inject the service
        // if there are service not created, create it, it will produce new readyToInject
        while (readyToInjects.isNotEmpty()) {
            val current = readyToInjects.first()
            val currentDeclare = serviceDeclarations.findByDeclare(current.injectComponent.declareType)
            if (current.injectComponent.injectLazy) {
                val serviceProxy = ServiceProxyFactory(currentDeclare, storage, serviceDeclarations, scopeAware).createProxy()
                ServiceUtil.injectObjectProperty(current, serviceProxy)
            } else {
                // get the service by declaration
                var service = storage.findService(ServiceIdentifier.ofDeclare(currentDeclare, scope))
                if (service == null) {
                    service = createStub(currentDeclare, scope)
                }
                ServiceUtil.injectObjectProperty(current, service)
            }

            readyToInjects.remove(current)
        }

        return stub
    }

    /**
     * create the stub service, means the service on constructor has been injected.
     * @param stubs if not null, add the created stub to stubs, otherwise ignore the addition.
     */
    private fun createStub(declare: ServiceDeclare, scope: Scope?): Any {
        // get the constructor
        val constructor = declare.constructor
        val args = constructor.parameters.map {
            // if is lazyInject then inject the proxy object.
            if (ServiceUtil.hasAnnotation<Lazy>(it)) {
                ServiceProxyFactory(declare, storage, serviceDeclarations, scopeAware).createProxy()
            } else {
                // get the declare of the type
                val parameterDeclare = serviceDeclarations.findByDeclare(it.type.jvmErasure)
                this.createStub(parameterDeclare, scope)
            }

        }.toTypedArray()
        val stub: Any = constructor.javaConstructor!!.newInstance(*args)!!

        readyToInjects.addAll(extractStubToInjectProperties(stub))

        // to add the service into the storage
        storage.addService(ServiceIdentifier.ofDeclare(declare, scope), stub)

        return stub
    }

    // extract stub to property read to injected
    private fun extractStubToInjectProperties(value: Any): List<ServiceProperty> {
        val declare = serviceDeclarations.findByService(value.javaClass.kotlin)
        return declare.toInjectServiceProperties(value)
    }


}