package org.tty.dioc.core.util

import org.tty.dioc.annotation.*
import org.tty.dioc.core.declare.*
import org.tty.dioc.error.ServiceConstructException
import org.tty.dioc.reflect.*
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses

object ServiceUtil {
    /**
     * to detect whether class has [Component] annotation
     */
    val KClass<*>.isComponent: Boolean
        get() = try {
            hasAnnotation<Component>()
        } catch (e: UnsupportedOperationException) {
            false
        }

    /**
     * to detect whether class has [InternalComponent] annotation
     */
    val KClass<*>.isInternalComponent: Boolean
        get() = try {
            hasAnnotation<InternalComponent>()
        } catch (e: UnsupportedOperationException) {
            false
        }

    /**
     * to detect whether a service is a proxy service
     */
    fun <T : Any> detectProxy(service: T): Boolean {
        return service is Proxy
    }

    fun isValidName(name: String): Boolean {
        return name.matches(Regex("^[A-Za-z0-9._-]+$"))
    }

    /**
     * returns the interfaces and superclass(not any) directly implemented by the class.
     * if there's no interfaces and superclass, return itself
     * @param toExcept the expected interfaces on scan.
     */
    fun indexTypes(realType: KClass<*>, toExcept: List<KClass<*>> = listOf()): List<KClass<*>> {
        val list = realType.superclasses.filter { !(it == Any::class || toExcept.contains(it)) }
        return list.ifEmpty {
            listOf(realType)
        }
    }

    /**
     * inject the component [component] to [ComponentProperty.service]
     */
    fun inject(componentProperty: ComponentProperty, component: Any) {
        val property = componentProperty.service::class.getProperty<KMutableProperty<*>>(componentProperty.name)!!
        property.setter.call(componentProperty.service, component)
    }

    /**
     * get the components of the [realType]
     */
    fun components(realType: KClass<*>): List<PropertyComponent> {
        return componentsOfConstructor(realType)
            .plus(componentsOfProperties(realType))
    }

    /**
     * get inject constructor of the [realType]
     * @see [InjectConstructor]
     */
    fun injectConstructor(realType: KClass<*>): KFunction<*> {
        return when {
            realType.constructors.isEmpty() -> {
                throw ServiceConstructException("no public constructor.")
            }
            realType.constructors.singleOrNull() != null -> {
                realType.constructors.single()
            }
            else -> {
                realType.constructors.singleOrNull { it2 -> it2.hasAnnotation<InjectConstructor>() }
            }
        } ?: throw ServiceConstructException("there are more than one constructors has @InjectConstructor.")
    }

    /**
     * get the components on the constructor
     */
    private fun componentsOfConstructor(realType: KClass<*>): List<PropertyComponent> {
        val constructor = injectConstructor(realType)

        return constructor.parameters.map { parameter ->
            PropertyComponent(
                parameter.name!!,
                parameter.kotlin,
                InjectPlace.Constructor,
                parameter.hasAnnotation<Lazy>()
            )
        }
    }

    /**
     * get the components on the properties
     */
    private fun componentsOfProperties(realType: KClass<*>): List<PropertyComponent> {
        val properties = realType.properties
        return properties.filter {
            it.hasAnnotationOnPropertyOrSetter<Inject>()
        }.map {
            val isLazy = it.hasAnnotationOnPropertyOrSetter<Lazy>()
            PropertyComponent(it.name, it.returnTypeKotlin, injectPlace = InjectPlace.InjectProperty, isLazy)
        }
    }


}