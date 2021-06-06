package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.error.ServiceConstructorException
import java.beans.PropertyDescriptor
import java.lang.reflect.Constructor
import kotlin.reflect.KType

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

    fun getComponents(type: Class<*>): List<PropertyComponent> {
        return getComponentsOfConstructor(type)
            .plus(getComponentsOfProperties(type))
    }

    fun getComponentsOfConstructor(type: Class<*>): List<PropertyComponent> {
        var constructor: Constructor<*>? = null
        constructor = when {
            type.constructors.isEmpty() -> {
                throw ServiceConstructorException("no public constructor.")
            }
            type.constructors.singleOrNull() != null -> {
                type.constructors.single()
            }
            else -> {
                type.constructors.singleOrNull { it2 -> it2.annotations.any { it is InjectConstructor } }
            }
        }
        if (constructor == null){
            throw ServiceConstructorException("there are more than one constructors has @InjectConstructor.")
        }

        return constructor.parameters.map { parameter ->
            PropertyComponent(parameter.name, parameter.type, InjectPlace.Constructor)
        }
    }
    fun getComponentsOfProperties(type: Class<*>): List<PropertyComponent> {
        return type.declaredFields.map { it2 ->
            if (it2.annotations.any { it is Inject }) {
                PropertyComponent(it2.name, it2.type, InjectPlace.InjectProperty)
            } else {
                PropertyComponent(it2.name, it2.type, InjectPlace.Property)
            }
        }
    }
}