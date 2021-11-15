package org.tty.dioc.core.declare

import org.tty.dioc.annotation.*
import org.tty.dioc.base.InitializeAware
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.basic.ScopeAbility
import org.tty.dioc.core.key.ComponentKey
import org.tty.dioc.core.key.ScopeKey
import org.tty.dioc.core.key.SingletonKey
import org.tty.dioc.core.key.TransientKey
import org.tty.dioc.core.util.ServiceUtil
import org.tty.dioc.core.util.ServiceUtil.isComponent
import org.tty.dioc.core.util.ServiceUtil.isInternalComponent
import org.tty.dioc.error.ServiceConstructException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

/**
 * declaration of the service
 * @see [Component]
 * @see [InternalComponent]
 */
class ComponentDeclare(
    /**
     * identify of the component.
     */
    val name: String,
    /**
     * the real service type.
     */
    val realType: KClass<*>,
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
    init {
//        require(name == "" || ServiceUtil.isValidName(name)) {
//            "invalid key. should use \"^[A-Za-z0-9._-]+$\""
//        }
    }

    fun createKey(scope: Scope?): ComponentKey {
        require(name == "" || ServiceUtil.isValidName(name)) {
            "invalid key. should use \"^[A-Za-z0-9._-]+$\""
        }
        return when (lifecycle) {
            Lifecycle.Singleton -> {
                SingletonKey(realType, name, internal)
            }
            Lifecycle.Scoped -> {
                if (scope == null) {
                    throw ServiceConstructException("you couldn't get a scoped service out of a scope.")
                }
                ScopeKey(realType, name, scope)
            }
            Lifecycle.Transient -> {
                TransientKey(realType, name)
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
        return "${realType},${lifecycle},${if (isLazyComponent) "lazy" else "not lazy"}"
    }

    companion object {
        /**
         * get the [ComponentDeclare] from [realType]
         */
        fun fromType(realType: KClass<*>): ComponentDeclare {
            // you must annotate [@Component] on indexType.
            require(realType.isComponent) {
                "realType $realType is not a component."
            }

            val exceptInterfaces = listOf(
                InitializeAware::class, Scope::class, ScopeAbility::class,
                Iterable::class,
            )
            val indexTypes = ServiceUtil.indexTypes(realType, exceptInterfaces)
            val componentAnnotation = realType.findAnnotation<Component>()!!
            val constructor = ServiceUtil.injectConstructor(realType)
            val components = ServiceUtil.components(realType)
            val name = componentAnnotation.name

//            require(name == "" || ServiceUtil.isValidName(name)) {
//                "invalid key. should use \"^[A-Za-z0-9._-]+$\""
//            }

            return ComponentDeclare(
                name = name,
                realType = realType,
                indexTypes = indexTypes,
                internal = false,
                lifecycle = componentAnnotation.lifecycle,
                isLazyComponent = componentAnnotation.lazy,
                constructor = constructor,
                components = components
            )
        }

        fun fromInternalComponentType(name: String, indexType: KClass<*>, realType: KClass<*>): ComponentDeclare {
            require(indexType.isInternalComponent) {
                "indexType $realType is not a component."
            }
            val constructor = ServiceUtil.injectConstructor(realType)
            val components = ServiceUtil.components(realType)
//            require(/*name == "" ||*/ ServiceUtil.isValidName(name)) {
//                "invalid key. should use \"^[A-Za-z0-9._-]+$\""
//            }
            require(name != "") {
                "key should not be empty."
            }

            return ComponentDeclare(
                name = name,
                realType = realType,
                indexTypes = listOf(indexType),
                internal = true,
                lifecycle = Lifecycle.Singleton,
                isLazyComponent = false,
                constructor = constructor,
                components = components
            )
        }


    }
}