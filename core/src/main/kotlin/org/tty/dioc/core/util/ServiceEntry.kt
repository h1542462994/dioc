package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.declare.identifier.ServiceIdentifier
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAware
import org.tty.dioc.core.lifecycle.ServiceProxyFactory
import org.tty.dioc.core.storage.CombinedServiceStorage
import org.tty.dioc.util.kotlin
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaConstructor

/**
 * the entry for create or get the service
 */

class ServiceEntry(
    private val serviceDeclarations: ServiceDeclares,
    val storage: CombinedServiceStorage,
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
        // check.
        serviceDeclarations.check(serviceDeclare)

        // return the provided service if exists.
        val s = storage.findService(ServiceIdentifier.ofDeclare(serviceDeclare, scope))
        if (s != null) {
            return s
        }

        // check the creating of scoped service.
        if (scope == null && serviceDeclare.lifecycle == Lifecycle.Scoped) {
            throw ServiceConstructException("you couldn't get a scoped service out a scope.")
        }

        // to begin a transaction for creating a service.
        storage.begin()
        // then create the stub
        val stub = createStub(serviceDeclare, scope)

        try {

            // to fill the service not full (ready to inject components.)
            while (!storage.isPartEmpty) {
                // get the created service
                val (identifier, current) = storage.partFirst
                current.notInjectedComponents.forEach {
                    it.fill(serviceDeclarations)
                    //val currentDeclare = serviceDeclarations.findByDeclare(current.injectComponent.declareType)
                    if (it.propertyComponent.injectLazy) {
                        val serviceProxy = ServiceProxyFactory(it.propertyServiceDeclare, this).createProxy()
                        ServiceUtil.injectComponentToService(it, serviceProxy)
                    } else {
                        // get the service by declaration
                        var service = storage.findService(ServiceIdentifier.ofDeclare(it.propertyServiceDeclare, scope))
                        if (service == null) {
                            if (storage.transientNotReady(it.propertyServiceDeclare)) {
//                        if (partStorage.isCreating(it.propertyServiceDeclare)) {
                                throw ServiceConstructException("find a cycle dependency link on transient service, it will cause a dead lock, because dependency link ${it.propertyServiceDeclare.serviceType} -> ... -> ${it.propertyServiceDeclare.serviceType}")
                            }
                            service = createStub(it.propertyServiceDeclare, scope)
                        }
                        ServiceUtil.injectComponentToService(it, service)
                    }
                }
                storage.moveToFull(identifier)
            }
            storage.commit()
        }  catch (e: Throwable) {
            storage.rollback()
            throw e
        }
        return stub
    }

    /**
     * create the stub service, means the service on constructor has been injected.
     */
    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    private fun createStub(declare: ServiceDeclare, scope: Scope?): Any {
        // check.
        serviceDeclarations.check(declare)

        // get the constructor
        val constructor = declare.constructor
        val serviceIdentifier = ServiceIdentifier.ofDeclare(declare, scope)

        // add the not created to marking
        storage.addEmpty(declare)
        // create the stub recursively
        val args = constructor.parameters.map {
            val parameter = declare.componentsOf(InjectPlace.Constructor).find { component -> component.name == it.name }!!
            // get the declare of the type
            val parameterDeclare = serviceDeclarations.findByDeclare(parameter.declareType)
            // if is lazyInject then inject the proxy object.
            if (parameter.injectLazy) {
                ServiceProxyFactory(parameterDeclare, this).createProxy()
            } else {
                // the circle link check.
                if (
                // if the parameter is not created. then throw a exception
                    storage.notReady(parameterDeclare)) {
                    throw ServiceConstructException("you want to inject a service not created, it will cause dead lock, because dependency link ${parameterDeclare.serviceType} -> ... -> ${parameterDeclare.serviceType}")
                }
                // if the service is in the fullStorage, then return it.
                storage.findService(ServiceIdentifier.ofDeclare(parameterDeclare, scope)) ?:
                this.createStub(parameterDeclare, scope)
            }

        }.toTypedArray()
        val stub: Any = constructor.javaConstructor!!.newInstance(*args)!!

        val injects = extractStubToInjectProperties(stub)
        if (injects.isEmpty()) {
            storage.addFull(serviceIdentifier, declare, stub)
        } else {
            storage.addPart(
                serviceIdentifier,
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


}