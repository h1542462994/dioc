package org.tty.dioc.core.declare

import org.tty.dioc.core.util.ServiceUtil
import kotlin.reflect.KClass

/**
 * to represent service, to support the initialization of the instance.
 */
class ServiceElement(
    /**
     * the real service type.
     */
    val serviceType: KClass<*>,
    /**
     * the declaration service types.
     */
    val declarationTypes: List<KClass<*>>,
    /**
     * the declaration of the service
     */
    val serviceAnnotation: ServiceAnnotation,
) {


    companion object {
        fun fromType(type: KClass<*>): ServiceElement {
            require(ServiceUtil.detectService(type)) {
                "clazz is not a service"
            }
            val superTypes = ServiceUtil.superTypes(type)
            return ServiceElement(
                type,
                superTypes,
                ServiceAnnotation.fromType(type)
            )
        }
    }
}