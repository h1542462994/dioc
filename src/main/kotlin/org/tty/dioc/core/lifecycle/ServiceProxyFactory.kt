package org.tty.dioc.core.lifecycle

import org.tty.dioc.core.declare.ServiceDeclarations
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.error.ServiceConstructorException
import org.tty.dioc.core.storage.ServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import java.lang.reflect.Proxy

/**
 * service proxy. it means the service will only be created after invoke.
 */
class ServiceProxyFactory(
    private val serviceDeclare: ServiceDeclare,
    private val serviceStorage: ServiceStorage,
    private val serviceDeclarations: ServiceDeclarations,
    private val scopeAware: ScopeAware
) {
    fun createProxy(): Any {
        var finishCreate = false
        val creator = ServiceEntry<Any>(serviceStorage, serviceDeclarations, serviceDeclare, scopeAware.currentScope())
        val interfaces = serviceDeclare.serviceElement.declarationTypes.plus(ProxyService::class.java)
        var realObject: Any? = null
        val proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces) { _, method, args ->
            if (!finishCreate) {
                throw ServiceConstructorException("you could n't call proxy object when proxy is creating.")
            }
            if (realObject == null) {
                realObject = creator.getOrCreateService()
            }
            method.invoke(realObject, args)
        }

        finishCreate = true
        return proxy
    }
}