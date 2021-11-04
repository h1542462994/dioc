package org.tty.dioc.core.internal

import org.tty.dioc.annotation.InjectPlace
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.core.basic.ComponentResolver
import org.tty.dioc.core.declare.*
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.error.ServiceConstructException
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.lifecycle.ComponentProxyFactoryImpl
import org.tty.dioc.core.storage.CombinedComponentStorage
import org.tty.dioc.core.util.ServiceUtil
import kotlin.reflect.jvm.javaConstructor

/**
 * the entry for create or get the service
 */
class ComponentResolverImpl(
    private val serviceDeclarations: ReadonlyComponentDeclares,
    override val storage: CombinedComponentStorage,
    val scopeAbility: ScopeAbility
): ComponentResolver {

    // entry function for createService
    @Suppress("UNCHECKED_CAST")
    override fun <T> resolve(declare: ComponentDeclare): T {
        return resolve(declare, scopeAbility.currentScope()) as T
    }

    /**
     * the implementation of the creation of the service.
     */
    private fun resolve(componentDeclare: ComponentDeclare, scope: Scope?): Any  {
        // check.
        serviceDeclarations.check(componentDeclare)

        // return the provided service if exists.
        val s = storage.findComponent(ComponentKey.ofDeclare(componentDeclare, scope))
        if (s != null) {
            return s
        }

        // check the creating of scoped service.
        if (scope == null && componentDeclare.lifecycle == Lifecycle.Scoped) {
            throw ServiceConstructException("you couldn't get a scoped service out a scope.")
        }

        // to begin a transaction for creating a service.
        val transaction = storage.beginTransaction()

        try {
            // then create the stub
            val stub = createStub(componentDeclare, transaction, scope)
            // to fill the service not full (ready to inject components.)
            while (!storage.isPartEmpty) {
                // get the created service
                val (identifier, current) = storage.partFirst
                current.notInjectedComponents.forEach {
                    it.fill(serviceDeclarations)
//                    val currentDeclare = serviceDeclarations.findByDeclare(current.injectComponent.declareType)
                    if (it.propertyComponent.injectLazy) {
                        val serviceProxy = ComponentProxyFactoryImpl(it.propertyComponentDeclare, this).createProxy()
                        ServiceUtil.injectComponentToService(it, serviceProxy)
                    } else {
                        // get the service by declaration
                        var service = storage.findComponent(ComponentKey.ofDeclare(it.propertyComponentDeclare, scope))
                        if (service == null) {
                            if (transaction.transientNotReady(it.propertyComponentDeclare)) {
//                        if (partStorage.isCreating(it.propertyServiceDeclare)) {
                                throw ServiceConstructException("find a cycle dependency link on transient service, it will cause a dead lock, because dependency link ${it.propertyComponentDeclare.implementationType} -> ... -> ${it.propertyComponentDeclare.implementationType}")
                            }
                            service = createStub(it.propertyComponentDeclare, transaction, scope)
                        }
                        ServiceUtil.injectComponentToService(it, service)
                    }
                }
                transaction.moveToFull(identifier)
            }
            transaction.commit()
            return stub
        }  catch (e: Throwable) {
            transaction.rollback()
            throw e
        }
    }

    /**
     * create the stub service, means the service on constructor has been injected.
     */
    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    private fun createStub(declare: ComponentDeclare, transaction: CombinedComponentStorage.CreateTransaction, scope: Scope?): Any {
        // check.
        serviceDeclarations.check(declare)

        // get the constructor
        val constructor = declare.constructor
        val componentKey = ComponentKey.ofDeclare(declare, scope)

        // add the not created to marking
        transaction.addEmpty(declare)
        // create the stub recursively
        val args = constructor.parameters.map {
            val parameter = declare.componentsOf(InjectPlace.Constructor).find { component -> component.name == it.name }!!
            // get declaration of the type
            val parameterDeclare = serviceDeclarations.singleDeclarationType(parameter.declareType)
            // if is lazyInject then inject the proxy object.
            if (parameter.injectLazy) {
                ComponentProxyFactoryImpl(parameterDeclare, this).createProxy()
            } else {
                // the circle link check.
                if (
                // if the parameter is not created. then throw a exception
                    transaction.notReady(parameterDeclare)) {
                    throw ServiceConstructException("you want to inject a service not created, it will cause dead lock, because dependency link ${parameterDeclare.implementationType} -> ... -> ${parameterDeclare.implementationType}")
                }
                // if the service is in the fullStorage, then return it.
                storage.findComponent(ComponentKey.ofDeclare(parameterDeclare, scope)) ?:
                this.createStub(parameterDeclare, transaction, scope)
            }

        }.toTypedArray()
        val stub: Any = constructor.javaConstructor!!.newInstance(*args)!!

        val injects = extractStubToInjectProperties(stub)
        if (injects.isEmpty()) {
            transaction.addFull(
                componentKey,
                ServiceCreated(stub, declare)
            )
        } else {
            transaction.addPart(
                componentKey,
                ComponentCreating(stub, declare, ArrayList(injects))
            )
        }

        return stub
    }

    /**
     * extract stub to property read to injected
     */
    private fun extractStubToInjectProperties(value: Any): List<ComponentProperty> {
        val declare = serviceDeclarations.singleServiceType(value::class)
        return declare.toComponentProperties(value, injectPlace = InjectPlace.InjectProperty)
    }


}