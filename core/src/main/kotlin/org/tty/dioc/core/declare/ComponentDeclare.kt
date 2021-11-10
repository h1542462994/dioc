package org.tty.dioc.core.declare

import org.tty.dioc.annotation.Component
import org.tty.dioc.annotation.InjectPlace
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.Lifecycle
import org.tty.dioc.base.InitializeAware
import org.tty.dioc.core.lifecycle.ProxyService
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.basic.ScopeAbility
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.core.key.ScopeKey
import org.tty.dioc.core.key.SingletonKey
import org.tty.dioc.core.key.TransientKey
import org.tty.dioc.core.util.ServiceUtil
import org.tty.dioc.core.util.ServiceUtil.hasComponentAnnotation
import org.tty.dioc.error.ServiceConstructException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

/**
 * declaration of the service
 * @see [Component]
 */
class ComponentDeclare(
    /**
     * name of the component
     */
    val name: String?,
    /**
     * the real service type.
     */
    val implementationType: KClass<*>,
    /**
     * the declaration service types.
     */
    val indexTypes: List<KClass<*>>,
    /**
     * whether [InternalComponent]
     */
    val internal: Boolean,
    /**
     * the lifecycle of the service.
     * @see [Component.lifecycle]
     */
    val lifecycle: Lifecycle,
    /**
     * whether the service is a lazy service
     * @see [Component.lazy]
     */
    val isLazyComponent: Boolean,
    /**
     * the constructor for injection.
     */
    val constructor: KFunction<*>,
    /**
     * the components find in scan
     */
    val components: List<PropertyComponent>

) {
    fun createKey(scope: Scope?): ComponentKey {
        return when (lifecycle) {
            Lifecycle.Singleton -> {
                SingletonKey(implementationType, null, internal)
            }
            Lifecycle.Scoped -> {
                if (scope == null) {
                    throw ServiceConstructException("you couldn't get a scoped service out of a scope.")
                }
                ScopeKey(implementationType, null, scope)
            }
            Lifecycle.Transient -> {
                TransientKey(implementationType, null)
            }
        }
    }

    /**
     * get the serviceProperties which the injectPlace is equal to [injectPlace], relies on [componentsOf].
     */
    fun toComponentProperties(service: Any, injectPlace: InjectPlace): List<ComponentProperty> {
        return componentsOf(injectPlace = injectPlace).map {
            ComponentProperty(componentDeclare = this, service = service, it.name, injectPlace = injectPlace)
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
        return "${implementationType},${lifecycle},${if (isLazyComponent) "lazy" else "not lazy"}"
    }

    companion object {
        /**
         * to get the [ComponentDeclare] from [implementationType]
         */
        fun fromType(implementationType: KClass<*>): ComponentDeclare {
            // you must annotate [@Service] on serviceType.
            require(implementationType.hasComponentAnnotation) {
                "serviceType $implementationType is not a service"
            }
            val exceptInterfaces = listOf(
                InitializeAware::class, ProxyService::class, Scope::class, ScopeAbility::class
            )
            val declarationTypes = ServiceUtil.declareTypes(implementationType, exceptInterfaces)
            val componentAnnotation = implementationType.findAnnotation<Component>()!!
            val constructor = ServiceUtil.getInjectConstructor(implementationType)
            val components = ServiceUtil.getComponents(implementationType)

            return ComponentDeclare(
                name = null,
                implementationType = implementationType,
                indexTypes = declarationTypes,
                internal = false,
                lifecycle = componentAnnotation.lifecycle,
                isLazyComponent = componentAnnotation.lazy,
                constructor = constructor,
                components = components
            )
        }

        fun fromInternalComponentType(declarationType: KClass<*>, implementationType: KClass<*>): ComponentDeclare {
            return ComponentDeclare(
                name = null,
                implementationType = implementationType,
                indexTypes = listOf(declarationType),
                internal = true,
                lifecycle = Lifecycle.Singleton,
                isLazyComponent = false,
                constructor = ServiceUtil.getInjectConstructor(implementationType),
                components = ServiceUtil.getComponents(implementationType)
            )
        }


    }
}