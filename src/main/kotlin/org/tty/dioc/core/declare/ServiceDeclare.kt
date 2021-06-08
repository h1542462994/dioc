package org.tty.dioc.core.declare

import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.ProxyService
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAware
import org.tty.dioc.core.util.ServiceUtil
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * the declare of the service
 */
class ServiceDeclare(
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
     * @see [Service.lifecycle]
     */
    val lifecycle: Lifecycle,
    /**
     * whether the service is a lazy service
     * @see [Service.lazy]
     */
    val isLazyService: Boolean,
    /**
     * the constructor for injection.
     */
    val constructor: KFunction<*>,
    /**
     * the components find in scan
     */
    val components: List<PropertyComponent>

) {
    fun toServiceProperties(service: Any, injectPlace: InjectPlace): List<ServiceProperty> {
        return componentsOf(injectPlace = injectPlace).map {
            ServiceProperty(serviceDeclare = this, service = service, it.name, injectPlace = injectPlace)
        }
    }

    fun componentsOf(injectPlace: InjectPlace): List<PropertyComponent> {
        return components.filter { it.injectPlace == injectPlace }
    }

    companion object {
        fun fromType(serviceType: KClass<*>): ServiceDeclare {
            require(ServiceUtil.detectService(serviceType)) {
                "clazz is not a service"
            }
            val expectInterfaces = listOf(
                InitializeAware::class, ProxyService::class, Scope::class, ScopeAware::class
            )
            val declarationTypes = ServiceUtil.superTypes(serviceType)
            val serviceAnnotation = ServiceUtil.findAnnotation<Service>(serviceType)!!
            val constructor = ServiceUtil.getInjectConstructor(serviceType)
            val components = ServiceUtil.getComponents(serviceType)

            return ServiceDeclare(
                serviceType = serviceType,
                declarationTypes = declarationTypes,
                lifecycle = serviceAnnotation.lifecycle,
                isLazyService = serviceAnnotation.lazy,
                constructor = constructor,
                components = components
            )
        }

        fun List<ServiceDeclare>.findByDeclare(declareType: KClass<*>): ServiceDeclare {
            return this.single { it.declarationTypes.contains(declareType) }
        }

        fun List<ServiceDeclare>.findByService(serviceType: KClass<*>): ServiceDeclare {
            return this.single { it.serviceType == serviceType }
        }
    }
}