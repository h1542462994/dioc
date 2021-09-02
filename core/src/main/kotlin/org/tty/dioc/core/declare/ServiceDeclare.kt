package org.tty.dioc.core.declare

import org.tty.dioc.advice.InterfaceAdvice
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
 * declaration of the service
 * @see [Service]
 */
class ServiceDeclare(
    /**
     * the service declare is from [InterfaceAdvice]
     * TODO: ?not available?
     */
    val isInterfaceAdvice: Boolean,
    /**
     * the real service type.
     */
    val implementationType: KClass<*>,
    /**
     * the declaration service types.
     */
    val declarationTypes: List<KClass<*>>,
    /**
     * the lifecycle of the service.
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
     * get the serviceProperties which the injectPlace is equal to [injectPlace], relies on [componentsOf].
     */
    fun toServiceProperties(service: Any, injectPlace: InjectPlace): List<ServiceProperty> {
        return componentsOf(injectPlace = injectPlace).map {
            ServiceProperty(serviceDeclare = this, service = service, it.name, injectPlace = injectPlace)
        }
    }

    /**
     * get the propertyComponents which the injectPlace is equal to [injectPlace]
     */
    fun componentsOf(injectPlace: InjectPlace): List<PropertyComponent> {
        return components.filter { it.injectPlace == injectPlace }
    }

    /**
     * a short description of the service.
     */
    override fun toString(): String {
        return "${implementationType},${lifecycle},${if (isLazyService) "lazy" else "not lazy"}"
    }

    companion object {
        /**
         * to get the [ServiceDeclare] from [implementationType]
         */
        fun fromType(implementationType: KClass<*>): ServiceDeclare {
            // you must annotate [@Service] on serviceType.
            require(implementationType.hasServiceAnnotation) {
                "serviceType $implementationType is not a service"
            }
            val exceptInterfaces = listOf(
                InitializeAware::class, ProxyService::class, Scope::class, ScopeAbility::class
            )
            val declarationTypes = ServiceUtil.declareTypes(implementationType, exceptInterfaces)
            val serviceAnnotation = implementationType.findAnnotation<Service>()!!
            val constructor = ServiceUtil.getInjectConstructor(implementationType)
            val components = ServiceUtil.getComponents(implementationType)

            return ServiceDeclare(
                isInterfaceAdvice = false,
                implementationType = implementationType,
                declarationTypes = declarationTypes,
                lifecycle = serviceAnnotation.lifecycle,
                isLazyService = serviceAnnotation.lazy,
                constructor = constructor,
                components = components
            )
        }

    }
}