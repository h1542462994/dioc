package org.tty.dioc.core.lifecycle

import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.error.ServiceConstructorException
import org.tty.dioc.core.storage.ServiceStorage
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.core.util.ServiceUtil.toClasses
import java.lang.reflect.Proxy

/**
 * service proxy. it means the service will only be created after invoke.
 */
class ServiceProxyFactory(
    private val serviceDeclare: ServiceDeclare,
    private val serviceStorage: ServiceStorage,
    private val serviceDeclarations: List<ServiceDeclare>,
    private val scopeAware: ScopeAware
) {
    fun createProxy(): Any {
        var finishCreate = false
        val creator = ServiceEntry<Any>(serviceStorage, serviceDeclarations, serviceDeclare, scopeAware)
        val interfaces = serviceDeclare.declarationTypes.plus(ProxyService::class)
        var realObject: Any? = null

        val proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces.toClasses()) { _, method, args ->
            if (!finishCreate) {
                throw ServiceConstructorException("you could n't call proxy object when proxy is creating.")
            }
            if (realObject == null) {
                realObject = creator.getOrCreateService()
            }

            if (args == null) {
                method.invoke(realObject)
            } else {
                method.invoke(realObject, *args)
            }
        }

        finishCreate = true
        return proxy
    }
}