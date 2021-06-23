package org.tty.dioc.core.lifecycle

import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.declare.ServiceDeclare
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.util.ServiceEntry
import org.tty.dioc.util.toClasses
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy

/**
 * service proxy. it means the service will only be created after invoke.
 */
class ServiceProxyFactory(
    private val serviceDeclare: ServiceDeclare,
    private val serviceEntry: ServiceEntry
) {
    /**
     * create the proxy for service inject with [Lazy]
     */
    fun createProxy(): Any {

        /**
         * make the proxy implements service declarationTypes
         */
        val interfaces = serviceDeclare.declarationTypes.plus(ProxyService::class)

        /**
         * the realObject for proxy
         */
        var realObject: Any? = null

        val proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces.toClasses()) { _, method, args ->
            // if the service is creating, then throw a exception
            if (serviceEntry.storage.isCreatingService) {
                throw ServiceConstructException("you could n't call proxy object when proxy is creating.")
            }

            /**
             * the proxy object will be created after the first call
             */
            if (realObject == null) {
                realObject = serviceEntry.getOrCreateService(serviceDeclare)
            }

            /**
             * proxy the functions
             */
            if (args == null) {
                method.invoke(realObject)
            } else {
                method.invoke(realObject, *args)
            }
        }

        return proxy
    }
}