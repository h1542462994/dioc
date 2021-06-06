package org.tty.dioc.core.declare

import org.tty.dioc.core.util.ServiceUtil

/**
 * to represent service, to support the initialization of the instance.
 */
class ServiceElement(
    /**
     * the real service type.
     */
    val serviceType: Class<*>,
    /**
     * the declaration service types.
     */
    val declarationTypes: Array<Class<*>>,
    /**
     * the declaration of the service
     */
    val serviceAnnotation: ServiceAnnotation,
) {


    companion object {
        fun fromClazz(clazz: Class<*>): ServiceElement {
            require(ServiceUtil.detectService(clazz)) {
                "clazz is not a service"
            }
            val superTypes = ServiceUtil.superTypes(clazz)
            return ServiceElement(
                clazz,
                superTypes,
                ServiceAnnotation.fromClass(clazz)
            )
        }
    }
}