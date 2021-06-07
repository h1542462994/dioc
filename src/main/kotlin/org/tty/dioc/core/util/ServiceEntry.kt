package org.tty.dioc.core.util

import org.tty.dioc.core.declare.InjectPlace
import org.tty.dioc.core.declare.ServiceDeclarations
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.lifecycle.LifeCycle
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAware
import org.tty.dioc.core.lifecycle.ServiceProxyFactory
import org.tty.dioc.core.storage.ServiceStorage

/**
 * the entry for create or get the service
 */
class ServiceEntry<T>(
    private val storage: ServiceStorage,
    private val serviceDeclarations: ServiceDeclarations,
    private val type: ServiceDeclare,
    private val scopeAware: ScopeAware
) {


    // entry function for createService
    fun getOrCreateService(): T {
        return getOrCreateService(type, scopeAware.currentScope()) as T
    }

    // the object ready to injected
    private var readyToInjects = ArrayList<ObjectProperty>()


    /**
     * the implementation of the creation of the service.
     */
    private fun getOrCreateService(type: ServiceDeclare, scope: Scope?): Any  {
        // first return the provided service if exists.
        val s = getService(type, scope)
        if (s != null) {
            return s
        }

        // then create the stub
        val stub = createStub(type, scope)

        // then to inject the service
        // if there are service not created, create it, it will produce new readyToInject
        while (readyToInjects.isNotEmpty()) {
            val current = readyToInjects.first()
            if (current.propertyComponent.injectLazy) {
                val serviceProxy = ServiceProxyFactory(current.serviceDeclare, storage, serviceDeclarations, scopeAware).createProxy()
                ServiceUtil.injectObjectProperty(current, serviceProxy)
            } else {
                // get the service by declaration
                var service = this.getService(current.serviceDeclare, scope)
                if (service == null) {
                    service = createStub(current.serviceDeclare, scope)
                }
                ServiceUtil.injectObjectProperty(current, service)
            }



            readyToInjects.remove(current)
        }

        return stub
    }

    /**
     * the implementation of the creation of the transient service.
     */
    private fun createTransientService(type: ServiceDeclare, scope: Scope?, notInjected: ArrayList<ObjectProperty>): Any {
        // to create stub and not record the stub.
        val stub = createStub(type, scope)
        // first scan the properties
        // remain the singleton service injection to next turn.
        notInjected.addAll(type.singletonComponents.map {
            ObjectProperty(stub, it, type)
        })
        notInjected.addAll(type.scopedComponents.map {
            ObjectProperty(stub, it, type)
        })
        // inject the transient services.
        type.transientComponents.forEach {
            val objectProperty = ObjectProperty(stub, it, type)
            ServiceUtil.injectObjectProperty(objectProperty, createTransientService(objectProperty.serviceDeclare, scope, notInjected))
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
            if (ServiceUtil.isLazyInject(it)) {
                return ServiceProxyFactory(declare, storage, serviceDeclarations, scopeAware).createProxy()
            } else {
                // get the declare of the type
                val parameterDeclare = serviceDeclarations.findByDeclare(it.type)!!
                this.createStub(parameterDeclare, scope)
            }

        }
        val stub = constructor.create(args)

        readyToInjects.addAll(extractStubToInjects(stub))

        // to add the service into the storage
        storage.addService(declare, scope, stub)

        return stub
    }

    // extract stub to property read to injected
    private fun extractStubToInjects(value: Any): List<ObjectProperty> {
        val declare = serviceDeclarations.findByService(value.javaClass)!!
        val components = declare.componentsOn(InjectPlace.InjectProperty)
        return components.map { component ->
            ObjectProperty(value, component, serviceDeclarations.findByDeclare(component.type)!!)
        }
    }

    /**
     * get the service from the storage
     */
    private fun getService(type: ServiceDeclare, scope: Scope?): Any? {
        return when (type.lifeCycle) {
            LifeCycle.Singleton -> {
                storage.findSingleton(type.serviceElement.serviceType)
            }
            LifeCycle.Scoped -> {
                storage.findScoped(type.serviceElement.serviceType, scope!!)
            }
            else -> {
                return null
            }
        }
    }



    fun isConstructed(): Boolean {
        return ! readyToInjects.any { p -> p.service.javaClass == type.serviceElement.serviceType }
    }
}