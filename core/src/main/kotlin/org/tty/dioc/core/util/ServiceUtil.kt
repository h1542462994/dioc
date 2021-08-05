package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.util.*
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses

object ServiceUtil {
    /**
     * to detect whether a class is a service
     */
    val KClass<*>.hasServiceAnnotation: Boolean
    get() = this.hasAnnotation<Service>()

    /**
     * to detect whether a service is a proxy service
     */
    fun <T: Any> detectProxy(service: T): Boolean {
        return service is Proxy
    }

    /**
     * returns the interfaces and superclass(not any) directly implemented by the class.
     * if there's no interfaces and superclass, return itself
     * @param toExcept the expected interfaces on scan.
     */
    fun declareTypes(serviceType: KClass<*>, toExcept: List<KClass<*>> = listOf()): List<KClass<*>> {
        val list = serviceType.superclasses.filter { ! (it == Any::class || toExcept.contains(it)) }
        return list.ifEmpty {
            listOf(serviceType)
        }
    }

    /**
     * inject the component [component] to [ServiceProperty.service]
     */
    fun injectComponentToService(serviceProperty: ServiceProperty, component: Any) {
        val property = serviceProperty.service::class.getProperty<KMutableProperty<*>>(serviceProperty.name)!!
        property.setter.call(serviceProperty.service, component)
    }

    /**
     * get the components of the [serviceType]
     */
    fun getComponents(serviceType: KClass<*>): List<PropertyComponent> {
        return getComponentsOfConstructor(serviceType)
            .plus(getComponentsOfProperties(serviceType))
    }

    /**
     * get inject constructor of the [serviceType]
     * @see [InjectConstructor]
     */
    fun getInjectConstructor(serviceType: KClass<*>): KFunction<*> {
        return when {
            serviceType.constructors.isEmpty()  -> {
                throw ServiceConstructException("no public constructor.")
            }
            serviceType.constructors.singleOrNull() != null -> {
                serviceType.constructors.single()
            }
            else -> {
                serviceType.constructors.singleOrNull { it2 -> it2.hasAnnotation<InjectConstructor>() }
            }
        } ?: throw ServiceConstructException("there are more than one constructors has @InjectConstructor.")
    }

    /**
     * get the components on the constructor
     */
    fun getComponentsOfConstructor(serviceType: KClass<*>): List<PropertyComponent> {
        val constructor = getInjectConstructor(serviceType)

        return constructor.parameters.map { parameter ->
            PropertyComponent(parameter.name!!, parameter.kotlin, InjectPlace.Constructor, parameter.hasAnnotation<Lazy>())
        }
    }

    /**
     * get the components on the properties
     */
    fun getComponentsOfProperties(serviceType: KClass<*>): List<PropertyComponent> {
        val properties = serviceType.properties
        return properties.map {
            val isLazy = it.hasAnnotationOnPropertyOrSetter<Lazy>()
            val isInject = it.hasAnnotationOnPropertyOrSetter<Inject>()
            if (isInject) {
                PropertyComponent(it.name, it.returnTypeKotlin, injectPlace = InjectPlace.InjectProperty, isLazy)
            } else {
                PropertyComponent(it.name, it.returnTypeKotlin, injectPlace = InjectPlace.Property, isLazy)
            }
        }
    }


}