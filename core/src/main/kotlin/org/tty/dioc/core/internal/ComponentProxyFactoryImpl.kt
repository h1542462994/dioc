package org.tty.dioc.core.internal

import org.tty.dioc.core.basic.ComponentResolver
import org.tty.dioc.core.basic.ComponentProxyFactory
import org.tty.dioc.annotation.Lazy
import org.tty.dioc.core.declare.ComponentDeclare
import org.tty.dioc.error.ServiceConstructException
import org.tty.dioc.reflect.toClasses
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy

/**
 * service proxy. it means the service will only be created after invoke.
 */
internal class ComponentProxyFactoryImpl(
    private val componentDeclare: ComponentDeclare,
    private val serviceEntry: ComponentResolver
): ComponentProxyFactory {
    /**
     * create the proxy for service inject with [Lazy]
     */
    override fun createProxy(): Any {

        /**
         * make the proxy implements service declarationTypes
         */
        val interfaces = componentDeclare.indexTypes

        /**
         * the realObject for proxy
         */
        var realObject: Any? = null

        val proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), interfaces.toClasses())
        @kotlin.jvm.Throws(InvocationTargetException::class, ServiceConstructException::class)
        { _, method, args ->
            // if the service is creating, then throws exception
            if (serviceEntry.storage.anyTransaction()) {
                throw ServiceConstructException("you could n't call proxy object when proxy is creating.")
            }

//            if (serviceEntry.storage.findService(
//                    ServiceIdentifier.ofDeclare(
//                        serviceDeclare,
//                        serviceEntry.scopeAbility.currentScope()
//                    )
//            ) == null) {
//                throw ServiceConstructException("the proxy service is out of control.")
//            }

            /**
             * the proxy object will be created after the first call
             */
            if (realObject == null) {
                realObject = serviceEntry.resolve(componentDeclare)
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