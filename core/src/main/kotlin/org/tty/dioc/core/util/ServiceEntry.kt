package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAware
import org.tty.dioc.core.lifecycle.ServiceProxyFactory
import org.tty.dioc.core.storage.ServiceStorage
import kotlin.reflect.full.hasAnnotation
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
    @Suppress("UNCHECKED_CAST")
    fun getOrCreateService(): T {
        return getOrCreateService(type, scopeAware.currentScope()) as T
    }

    // the object ready to injected
    private var readyToInjects = ArrayList<ServiceProperty>()

    // the record of the transient service on creating.
    // TODO: 2021/6/8 add transient service record to avoid the call cycle
    private val readyTransients = readyToInjects.groupBy {
        it.serviceDeclare
    }.keys.filter { it.lifecycle == Lifecycle.Transient }

    private fun isReady(service: Any): Boolean {
        return ! readyToInjects.any { it.service == service }
    }

    /**
     * the implementation of the creation of the service.
     */
    private fun getOrCreateService(serviceDeclare: ServiceDeclare, scope: Scope?): Any  {
        // first return the provided service if exists.
        val s = storage.findService(ServiceIdentifier.ofDeclare(serviceDeclare, scope))
        if (s != null) {
            return s
        }

        if (scope == null && serviceDeclare.lifecycle == Lifecycle.Scoped) {
            throw ServiceConstructException("you couldn't get a scoped service out a scope.")
        }

        // then create the stub
        val stub = createStub(serviceDeclare, scope)

        // then to inject the service
        // if there are service not created, create it, it will produce new readyToInject
        while (readyToInjects.isNotEmpty()) {
            val current = readyToInjects.first()
            current.fill(serviceDeclarations)

            //val currentDeclare = serviceDeclarations.findByDeclare(current.injectComponent.declareType)
            if (current.propertyComponent.injectLazy) {
                val serviceProxy = ServiceProxyFactory(current.propertyServiceDeclare, storage, serviceDeclarations, scopeAware).createProxy()
                ServiceUtil.injectComponentToService(current, serviceProxy)
            } else {
                // get the service by declaration
                var service = storage.findService(ServiceIdentifier.ofDeclare(current.propertyServiceDeclare, scope))
                if (service == null) {
                    if (current.propertyServiceDeclare.lifecycle == Lifecycle.Singleton && readyTransients.contains(current.propertyServiceDeclare)) {
                        throw ServiceConstructException("find a cycle dependency link on transient service, it will cause a dead lock, because dependency link ${current.propertyServiceDeclare.serviceType} -> ... -> ${current.propertyServiceDeclare.serviceType}")
                    }
                    service = createStub(current.propertyServiceDeclare, scope)
                }
                ServiceUtil.injectComponentToService(current, service)
            }

            readyToInjects.remove(current)

            if (isReady(current)) {
                notifyServiceOnInit(stub)
            }
        }

        return stub
    }

    /**
     * create the stub service, means the service on constructor has been injected.
     * @param stubs if not null, add the created stub to stubs, otherwise ignore the addition.
     */
    private fun createStub(declare: ServiceDeclare, scope: Scope?, declareRecord: ArrayList<ServiceDeclare> = arrayListOf()): Any {
        // get the constructor
        val constructor = declare.constructor

        // to add the current declare to record
        declareRecord.add(declare)

        val args = constructor.parameters.map {
            // if is lazyInject then inject the proxy object.
            if (it.hasAnnotation<Lazy>()) {
                ServiceProxyFactory(declare, storage, serviceDeclarations, scopeAware).createProxy()
            } else {
                // get the declare of the type
                val parameterDeclare = serviceDeclarations.findByDeclare(it.type.jvmErasure)
                if (declareRecord.contains(parameterDeclare)) {
                    throw ServiceConstructException("you want to inject a service in creating, it will cause dead lock, because dependency link ${declare.serviceType} -> ... -> ${declare.serviceType}")
                }

                this.createStub(parameterDeclare, scope, declareRecord)
            }

        }.toTypedArray()
        val stub: Any = constructor.javaConstructor!!.newInstance(*args)!!

        // to remove the current declare from record
        declareRecord.remove(declare)

        val injects = extractStubToInjectProperties(stub)
        if (injects.isEmpty()) {
            notifyServiceOnInit(stub)
        }

        readyToInjects.addAll(injects)

        // to add the service into the storage
        storage.addService(ServiceIdentifier.ofDeclare(declare, scope), stub)

        return stub
    }

    // extract stub to property read to injected
    private fun extractStubToInjectProperties(value: Any): List<ServiceProperty> {
        val declare = serviceDeclarations.findByService(value::class)
        return declare.toServiceProperties(value, injectPlace = InjectPlace.InjectProperty)
    }

    /**
     * to notify the service if the service implements [InitializeAware]
     */
    private fun notifyServiceOnInit(service: Any) {
        if (service is InitializeAware) {
            service.onInit()
        }
    }

}