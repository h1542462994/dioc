package org.tty.dioc.core.util

import org.tty.dioc.core.declare.ServiceDeclarations
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.lifecycle.LifeCycle
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.storage.ServiceStorage

class ServiceCreator<T>(val storage: ServiceStorage, private val serviceDeclarations: ServiceDeclarations, val type: ServiceDeclare, private val scope: Scope? = null) {


    // entry function for createService
    fun getOrCreateService(): T {
        return getOrCreateService(type, scope) as T
    }

    /**
     * the implementation of the creation of the service.
     */
    @Suppress("UNCHECKED_CAST")
    private fun getOrCreateService(type: ServiceDeclare, scope: Scope?): Any  {

        // first return the provided service
        val s = getService(type, scope)
        if (s != null) {
            return s
        }

        // first create the stub.
        val stub = createStub(type, scope)

        // then inject the services.
        var notInjected = extractStubsToNotInjected()
        val injected = ArrayList<ObjectProperty>()
        val notInjectedInTransient = ArrayList<ObjectProperty>()

        notInjected.forEach {
            if (it.serviceDeclare.lifeCycle == LifeCycle.Singleton) {
                val service = storage.findSingleton(it.propertyComponent.type)
                if (service != null) {
                    injected.add(it)
                    ServiceUtil.injectObjectProperty(it, service)
                }
            } else if (it.serviceDeclare.lifeCycle == LifeCycle.Scoped) {
                val service = storage.findScoped(it.propertyComponent.type, scope!!)
                if (service != null) {
                    injected.add(it)
                    ServiceUtil.injectObjectProperty(it, service)
                }
            } else if (it.serviceDeclare.lifeCycle == LifeCycle.Transient) {
                val service = createTransientService(type, scope, notInjectedInTransient)
                injected.add(it)
                ServiceUtil.injectObjectProperty(it, service)
            }
        }

        // then calculate the not inject services
        notInjected = notInjected.subtract(injected).plus(notInjectedInTransient).toList()

        // then call self inclusively
        if (notInjected.isNotEmpty()) {
            notInjected.forEach { n ->
                ServiceUtil.injectObjectProperty(
                    n,
                    getOrCreateService(serviceDeclarations.findByDeclare(n.propertyComponent.type)!!, scope)
                )
            }
        }

        return stub

    }

    /**
     * the implementation of the creation of the transient service.
     */
    private fun createTransientService(type: ServiceDeclare, scope: Scope?, notInjected: ArrayList<ObjectProperty>): Any {
        // to create stub
        val stub = createStub(type, scope, toRecord = false)
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
     * @param toRecord whether to Record the stub to [stubs]
     */
    private fun createStub(type: ServiceDeclare, scope: Scope?, toRecord: Boolean = true): Any {
        // get the constructor
        val constructor = type.constructor
        val args = constructor.parameters.map {
            // get the declare of the type
            val declare = serviceDeclarations.findByDeclare(it.type)!!
            this.createStub(declare, scope)
        }
        val stub = constructor.create(args)

        if (toRecord) {
            stubs.add(stub)
        }

        // to add the service into the storage
        storage.addService(type, scope, stub)

        return stub
    }

    /**
     * the transientServices been created.
     */
    private var stubs = ArrayList<Any>()

    private fun extractStubsToNotInjected(): List<ObjectProperty> {
        val properties = singletonProperties
            .plus(scopedProperties)
            .plus(transientProperties)
        stubs.clear()
        return properties;
    }
    private val singletonProperties: List<ObjectProperty>
        get() = stubs.flatMap { value ->
        val declare = serviceDeclarations.findByService(value.javaClass)!!
        declare.singletonComponents.map { component ->
            ObjectProperty(value, component, declare)
        }
    }

    private val scopedProperties: List<ObjectProperty>
        get() = stubs.flatMap { value ->
            val declare = serviceDeclarations.findByService(value.javaClass)!!
            declare.transientComponents.map { component ->
                ObjectProperty(value, component, declare)
            }
        }

    private val transientProperties: List<ObjectProperty>
        get() = stubs.flatMap { value ->
            val declare = serviceDeclarations.findByService(value.javaClass)!!
            declare.transientComponents.map { component ->
                ObjectProperty(value, component, declare)
            }
        }

    @Suppress("UNCHECKED_CAST")
    fun getService(type: ServiceDeclare, scope: Scope?): T? {
        return when (type.lifeCycle) {
            LifeCycle.Singleton -> {
                storage.findSingleton(type.serviceElement.serviceType as Class<T>)
            }
            LifeCycle.Scoped -> {
                storage.findScoped(type.serviceElement.serviceType as Class<T>, scope!!)
            }
            else -> {
                return null
            }
        }
    }


}