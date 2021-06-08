package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.error.ServiceConstructException
import java.lang.reflect.Proxy
import kotlin.reflect.*
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmErasure

object ServiceUtil {
    /**
     * to detect whether a class is a service
     */
    fun detectService(serviceType: KClass<*>): Boolean {
        return hasAnnotation<Service>(serviceType)
    }

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
    fun injectObjectProperty(serviceProperty: ServiceProperty, component: Any) {
        val property = getProperty<KMutableProperty<*>>(serviceProperty.service::class, serviceProperty.name)!!
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
     * get the inject constructor of the [serviceType]
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
                serviceType.constructors.singleOrNull { it2 -> hasAnnotation<InjectConstructor>(it2) }
            }
        } ?: throw ServiceConstructException("there are more than one constructors has @InjectConstructor.")
    }

    /**
     * get the components on the constructor
     */
    fun getComponentsOfConstructor(serviceType: KClass<*>): List<PropertyComponent> {
        val constructor = getInjectConstructor(serviceType)

        return constructor.parameters.map { parameter ->
            PropertyComponent(parameter.name!!, parameter.type.jvmErasure, InjectPlace.Constructor, hasAnnotation<Lazy>(parameter))
        }
    }

    /**
     * get the components on the properties
     */
    fun getComponentsOfProperties(serviceType: KClass<*>): List<PropertyComponent> {
        val properties = serviceType.properties
        return properties.map {
            val isLazy = hasAnnotationOnPropertyOrSetter<Lazy>(it)
            val isInject = hasAnnotationOnPropertyOrSetter<Inject>(it)
            if (isInject) {
                PropertyComponent(it.name, it.returnType.jvmErasure, injectPlace = InjectPlace.InjectProperty, isLazy)
            } else {
                PropertyComponent(it.name, it.returnType.jvmErasure, injectPlace = InjectPlace.Property, isLazy)
            }
        }
    }

    /**
     * to detect whether property has the annotation [T]
     */
    inline fun <reified T: Annotation> hasAnnotationOnPropertyOrSetter(property: KProperty<*>): Boolean {
        return if (property is KMutableProperty<*>) {
            hasAnnotation<T>(property) || hasAnnotation<T>(property.setter)
        } else {
            hasAnnotation<T>(property)
        }
    }

    inline fun <reified T> hasAnnotation(element: KAnnotatedElement): Boolean {
        return element.annotations.any { it is T }
    }

    inline fun <reified T: Annotation> findAnnotation(element: KAnnotatedElement): T? {
        return element.annotations.filterIsInstance<T>().singleOrNull()
    }

    inline fun <reified T, reified TP: KCallable<*>> getProperty(name: String): TP? {
        return getProperty(T::class, name)
    }

    inline fun <reified TP: KCallable<*>> getProperty(type: KClass<*>, name: String): TP? {
        return type.members.filterIsInstance<TP>().singleOrNull { it.name == name }
    }

    val KClass<*>.properties: List<KProperty<*>>
    get() {
        return this.members.filterIsInstance<KProperty<*>>()
    }

    fun List<KClass<*>>.toClasses(): Array<out Class<*>> {
        return this.map { it.java }.toTypedArray()
    }
}