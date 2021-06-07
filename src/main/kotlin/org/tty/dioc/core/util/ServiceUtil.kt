package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.error.ServiceConstructorException
import org.tty.dioc.core.lifecycle.ProxyService
import java.beans.PropertyDescriptor
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Constructor
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

object ServiceUtil {
    /**
     * to detect whether a class is a service
     */
    fun detectService(type: Class<*>): Boolean {
        return type.annotations.any { it is Service }
    }

    /**
     * returns the interfaces and superclass(not any) directly implemented by the class.
     * if there's no interfaces and superclass, return itself
     */
    fun superTypes(type: Class<*>): Array<Class<*>> {
        var list = type.interfaces
        if (type.superclass !is Any) {
            list = list.plus(type.superclass)
        }
        return if (list.isNotEmpty()) {
            list
        } else {
            arrayOf(type)
        }
    }

    fun injectObjectProperty(objectProperty: ObjectProperty, toInject: Any) {
        val descriptor = PropertyDescriptor(objectProperty.propertyComponent.name, objectProperty.service.javaClass)
        descriptor.writeMethod.invoke(objectProperty.service, toInject)
    }

    fun getComponents(type: Class<*>): List<PropertyComponent> {
        return getComponentsOfConstructor(type)
            .plus(getComponentsOfProperties(type))
    }

    fun getInjectConstructor(type: Class<*>): Constructor<*> {
        return when {
            type.constructors.isEmpty() -> {
                throw ServiceConstructorException("no public constructor.")
            }
            type.constructors.singleOrNull() != null -> {
                type.constructors.single()
            }
            else -> {
                type.constructors.singleOrNull { it2 -> hasAnnotation<InjectConstructor>(it2) }
            }
        } ?: throw ServiceConstructorException("there are more than one constructors has @InjectConstructor.")
    }

    fun getComponentsOfConstructor(type: Class<*>): List<PropertyComponent> {
        val constructor = getInjectConstructor(type)

        return constructor.parameters.map { parameter ->
            PropertyComponent(parameter.name, parameter.type, InjectPlace.Constructor, hasAnnotation<Lazy>(parameter))
        }
    }

    fun getComponentsOfProperties(type: Class<*>): List<PropertyComponent> {
        val members = type.fields
        var t: Any = type
        try {
            t = type.kotlin
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return members.map {
            val isInject = hasAnnotationOn<Inject>(t, it.name)
            val isLazyInject = hasAnnotationOn<Lazy>(t, it.name)
            if (isInject) {
                PropertyComponent(it.name, it.type, InjectPlace.InjectProperty, isLazyInject)
            } else {
                PropertyComponent(it.name, it.type, InjectPlace.Property, isLazyInject)
            }
        }
    }

    /**
     * to judge whether the service is a proxy service.
     */
    fun isProxyService(any: Any):Boolean {
        return ProxyService::class.java.isAssignableFrom(any.javaClass)
    }


    inline fun <reified T> hasAnnotationOn(type: Any, property: String): Boolean {
        return when (type) {
            is KClass<*> -> {
                val field = type.java.getField(property)
                val p = type.members.filterIsInstance<KMutableProperty<*>>().single { it.name == property }
                hasAnnotation<T>(field) || hasAnnotation<T>(p) || hasAnnotation<T>(p.setter)
            }
            is Class<*> -> {
                val propertyDescriptor = PropertyDescriptor(property, type)
                val field = type.getField(property)
                hasAnnotation<T>(field) || hasAnnotation<T>(propertyDescriptor.writeMethod)
            }
            else -> {
                throw IllegalArgumentException("element should be Class<*> or KClass<*>")
            }
        }
    }

    inline fun <reified T> hasAnnotation(element: Any): Boolean {
        if (element is Class<*>) {
            return hasAnnotation<T>(element)
        } else if (element is KClass<*>) {
            return hasAnnotation<T>(element)
        }
        throw IllegalArgumentException("element should be Class<*> or KClass<*>")
    }

    inline fun <reified T> hasAnnotation(element: AnnotatedElement): Boolean {
        return element.annotations.any { it is T }
    }

    inline fun <reified T> hasAnnotation(element: KAnnotatedElement): Boolean {
        return element.annotations.any { it is T }
    }

}