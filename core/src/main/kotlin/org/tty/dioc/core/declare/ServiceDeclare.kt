package org.tty.dioc.core.declare

import org.tty.dioc.core.advice.InterfaceAdvice
import org.tty.dioc.core.error.ServiceDeclarationException
import org.tty.dioc.core.lifecycle.InitializeAware
import org.tty.dioc.core.lifecycle.ProxyService
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.util.ServiceUtil
import org.tty.dioc.core.util.ServiceUtil.hasServiceAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

/**
 * the declare of the service
 * @see [Service]
 */
class ServiceDeclare(
    /**
     * the service declare is from [InterfaceAdvice]
     */
    val isInterfaceAdvice: Boolean,
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
    /**
     * get the serviceProperties which the injectPlace is equal to [injectPlace]
     */
    fun toServiceProperties(service: Any, injectPlace: InjectPlace): List<ServiceProperty> {
        return componentsOf(injectPlace = injectPlace).map {
            ServiceProperty(serviceDeclare = this, service = service, it.name, injectPlace = injectPlace)
        }
    }

    /**
     * get the propertyComponent
     */
    fun componentsOf(injectPlace: InjectPlace): List<PropertyComponent> {
        return components.filter { it.injectPlace == injectPlace }
    }

    override fun toString(): String {
        return "${serviceType},${lifecycle},${if (isLazyService) "lazy" else "not lazy"}"
    }

    companion object {
        /**
         * to get the [ServiceDeclare] from [serviceType]
         */
        fun fromType(serviceType: KClass<*>): ServiceDeclare {
            require(serviceType.hasServiceAnnotation) {
                "serviceType $serviceType is not a service"
            }
            val exceptInterfaces = listOf(
                InitializeAware::class, ProxyService::class, Scope::class, ScopeAbility::class
            )
            val declarationTypes = ServiceUtil.declareTypes(serviceType, exceptInterfaces)
            val serviceAnnotation = serviceType.findAnnotation<Service>()!!
            val constructor = ServiceUtil.getInjectConstructor(serviceType)
            val components = ServiceUtil.getComponents(serviceType)

            return ServiceDeclare(
                isInterfaceAdvice = false,
                serviceType = serviceType,
                declarationTypes = declarationTypes,
                lifecycle = serviceAnnotation.lifecycle,
                isLazyService = serviceAnnotation.lazy,
                constructor = constructor,
                components = components
            )
        }
        fun fromDeclareAdvice(declarationType: KClass<*>): ServiceDeclare {
            val service = declarationType.findAnnotation<Service>()
                ?: throw ServiceDeclarationException("interface advice should be mark as @Service")
            val interfaceAdvice = declarationType.findAnnotation<InterfaceAdvice>()!!
            val serviceType = interfaceAdvice.serviceType
            val constructor = ServiceUtil.getInjectConstructor(serviceType)
            val components = ServiceUtil.getComponents(serviceType)
            return ServiceDeclare(
                isInterfaceAdvice = true,
                serviceType = serviceType,
                declarationTypes = listOf(declarationType),
                lifecycle = service.lifecycle,
                isLazyService = service.lazy,
                constructor = constructor,
                components = components
            )


        }

    }
}