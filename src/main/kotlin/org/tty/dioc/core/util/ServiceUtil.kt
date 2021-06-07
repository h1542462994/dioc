package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.error.ServiceConstructorException
import org.tty.dioc.core.lifecycle.ProxyService
import java.beans.PropertyDescriptor
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Parameter

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
                type.constructors.singleOrNull { it2 -> it2.annotations.any { it is InjectConstructor } }
            }
        } ?: throw ServiceConstructorException("there are more than one constructors has @InjectConstructor.")
    }

    fun getComponentsOfConstructor(type: Class<*>): List<PropertyComponent> {
        val constructor = getInjectConstructor(type)

        return constructor.parameters.map { parameter ->
            PropertyComponent(parameter.name, parameter.type, InjectPlace.Constructor, isLazyInject(parameter))
        }
    }

    fun getComponentsOfProperties(type: Class<*>): List<PropertyComponent> {
        return type.declaredFields.map { it2 ->
            if (it2.annotations.any { it is Inject }) {
                PropertyComponent(it2.name, it2.type, InjectPlace.InjectProperty, isLazyInject(it2))
            } else {
                PropertyComponent(it2.name, it2.type, InjectPlace.Property, isLazyInject(it2))
            }
        }
    }

    /**
     * to judge whether the service is a proxy service.
     */
    fun isProxyService(any: Any):Boolean {
        return ProxyService::class.java.isAssignableFrom(any.javaClass)
    }

    /**
     * to judge whether the parameter is annotated [Lazy]
     */
    fun isLazyInject(element: AnnotatedElement): Boolean {
        return element.annotations.any { it is Lazy }
    }



}