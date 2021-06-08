package org.tty.dioc.core.util

import org.tty.dioc.core.declare.*
import org.tty.dioc.core.error.ServiceConstructorException
import java.lang.reflect.Proxy
import kotlin.reflect.*
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.jvmErasure

object ServiceUtil {
    /**
     * to detect whether a class is a service
     */
    fun detectService(type: KClass<*>): Boolean {
        return hasAnnotation<Service>(type)
    }

    fun <T: Any> detectProxy(service: T): Boolean {
        return service is Proxy
    }

    /**
     * returns the interfaces and superclass(not any) directly implemented by the class.
     * if there's no interfaces and superclass, return itself
     */
    fun superTypes(type: KClass<*>, toExcept: List<KClass<*>> = listOf()): List<KClass<*>> {
        val list = type.superclasses.filter { ! (it == Any::class || toExcept.contains(it)) }
        return list.ifEmpty {
            listOf(type)
        }
    }

    /**
     * inject the service to the property
     */
    fun injectObjectProperty(serviceProperty: ServiceProperty, toInject: Any) {
        val property = getProperty<KMutableProperty<*>>(serviceProperty.service::class, serviceProperty.name)!!
        property.setter.call(serviceProperty.service, toInject)
    }

    fun getComponents(type: KClass<*>): List<PropertyComponent> {
        return getComponentsOfConstructor(type)
            .plus(getComponentsOfProperties(type))
    }

    fun getInjectConstructor(type: KClass<*>): KFunction<*> {
        return when {
            type.constructors.isEmpty()  -> {
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

    fun getComponentsOfConstructor(type: KClass<*>): List<PropertyComponent> {
        val constructor = getInjectConstructor(type)

        return constructor.parameters.map { parameter ->
            PropertyComponent(parameter.name!!, parameter.type.jvmErasure, InjectPlace.Constructor, hasAnnotation<Lazy>(parameter))
        }
    }

    fun getComponentsOfProperties(type: KClass<*>): List<PropertyComponent> {
        val properties = type.properties
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


    inline fun <reified T> hasAnnotationOnPropertyOrSetter(property: KProperty<*>): Boolean {
        if (property is KMutableProperty<*>) {
            return hasAnnotation<T>(property) || hasAnnotation<T>(property.setter)
        } else {
            return hasAnnotation<T>(property)
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