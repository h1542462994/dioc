package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAware
import org.tty.dioc.core.lifecycle.ServiceProxyFactory
import org.tty.dioc.core.storage.CreatingServiceStorage
import org.tty.dioc.core.storage.ServiceStorage
import org.tty.dioc.util.kotlin
import java.security.Provider
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaConstructor

/**
 * the entry for create or get the service
 */

class ServiceEntry(
    private val serviceDeclarations: ServiceDeclares,
    private val partStorage: CreatingServiceStorage,
    private val fullStorage: ServiceStorage,
    private val scopeAware: ScopeAware
) {


    // entry function for createService
    @Suppress("UNCHECKED_CAST")
    fun <T> getOrCreateService(type: ServiceDeclare): T {
        return getOrCreateService(type, scopeAware.currentScope()) as T
    }
    /**
     * the implementation of the creation of the service.
     */
    private fun getOrCreateService(serviceDeclare: ServiceDeclare, scope: Scope?): Any  {
        // first return the provided service if exists.
        val s = fullStorage.findService(ServiceIdentifier.ofDeclare(serviceDeclare, scope))
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
        while (!partStorage.isEmpty()) {
            // is creating
            val current = partStorage.first() as ServiceCreating
            current.notInjectedComponents.forEach {
                it.fill(serviceDeclarations)
                //val currentDeclare = serviceDeclarations.findByDeclare(current.injectComponent.declareType)
                if (it.propertyComponent.injectLazy) {
                    val serviceProxy = ServiceProxyFactory(it.propertyServiceDeclare, this).createProxy()
                    ServiceUtil.injectComponentToService(it, serviceProxy)
                } else {
                    // get the service by declaration
                    var service = fullStorage.findService(ServiceIdentifier.ofDeclare(it.propertyServiceDeclare, scope))
                    if (service == null) {
                        if (partStorage.isCreating(it.propertyServiceDeclare)) {
                            throw ServiceConstructException("find a cycle dependency link on transient service, it will cause a dead lock, because dependency link ${current.propertyServiceDeclare.serviceType} -> ... -> ${current.propertyServiceDeclare.serviceType}")
                        }
                        service = createStub(it.propertyServiceDeclare, scope)
                    }
                    ServiceUtil.injectComponentToService(it, service)
                }
            }

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
        val serviceIdentifier = ServiceIdentifier.ofDeclare(declare, scope)

        // create the stub recursively
        val args = constructor.parameters.map {
            // if is lazyInject then inject the proxy object.
            if (it.hasAnnotation<Lazy>()) {
                ServiceProxyFactory(declare, this).createProxy()
            } else {
                // get the declare of the type
                val parameterDeclare = serviceDeclarations.findByDeclare(it.kotlin)
                if (
                    partStorage.find(ServiceIdentifier.ofDeclare(parameterDeclare, scope)) != null &&
                    parameterDeclare.lifecycle == Lifecycle.Transient) {
                    throw ServiceConstructException("you want to inject a service in creating, it will cause dead lock, because dependency link ${declare.serviceType} -> ... -> ${declare.serviceType}")
                }

                this.createStub(parameterDeclare, scope)
            }

        }.toTypedArray()
        val stub: Any = constructor.javaConstructor!!.newInstance(*args)!!

        val injects = extractStubToInjectProperties(stub)
        if (injects.isEmpty()) {
            addFull(serviceIdentifier, stub)
        } else {
            addPart(
                ServiceCreating(stub, declare, ArrayList(injects))
            )
        }

        return stub
    }

    // extract stub to property read to injected
    private fun extractStubToInjectProperties(value: Any): List<ServiceProperty> {
        val declare = serviceDeclarations.findByService(value::class)
        return declare.toServiceProperties(value, injectPlace = InjectPlace.InjectProperty)
    }



    private fun addFull(identifier: ServiceIdentifier, service: Any){
        fullStorage.addService(identifier, service)
        notifyServiceOnInit(service)
    }

    private fun addPart(serviceCreating: ServiceCreating) {
        partStorage.add(serviceCreating)
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