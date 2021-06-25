package org.tty.dioc.util

import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

/**
 * has the annotation on property or setter
 */
inline fun <reified T: Annotation> KProperty<*>.hasAnnotationOnPropertyOrSetter(): Boolean {
    return if (this is KMutableProperty<*>) {
        this.hasAnnotation<T>() || this.setter.hasAnnotation<T>()
    } else {
        this.hasAnnotation<T>()
    }
}

/**
 * has the annotation on property or getter
 */
inline fun <reified T: Annotation> KProperty<*>.hasAnnotationOnPropertyOrGetter(): Boolean {
    return this.hasAnnotation<T>() || this.getter.hasAnnotation<T>()
}

/**
 * find the annotation on property or setter
 */
inline fun <reified T: Annotation> KProperty<*>.findAnnotationOnPropertyOrSetter(): T? {
    return if (this is KMutableProperty<*>) {
        val annotation1 = this.findAnnotation<T>()
        val annotation2 = this.setter.findAnnotation<T>()
        annotation1 ?: annotation2
    } else {
        this.findAnnotation()
    }
}

/**
 * find the annotation on property or getter
 */
inline fun <reified T: Annotation> KProperty<*>.findAnnotationOnPropertyOrGetter(): T? {
    val annotation1 = this.findAnnotation<T>()
    val annotation2 = this.getter.findAnnotation<T>()
    return annotation1 ?: annotation2
}

/**
 * get the property
 */
inline fun <reified TP: KProperty<*>> KClass<*>.getProperty(name: String): TP? {
    return this.members.filterIsInstance<TP>().singleOrNull { it.name == name }
}

/**
 * get the method
 */
inline fun <reified TP: KCallable<*>> KClass<*>.getMethod(name: String): TP? {
    return this.members.filterIsInstance<TP>().singleOrNull { it.name == name }
}


/**
 * the properties
 */
val KClass<*>.properties: List<KProperty<*>>
    get() {
        return this.members.filterIsInstance<KProperty<*>>()
    }

/**
 * the mutable Properties
 */
val KClass<*>.mutableProperties: List<KMutableProperty<*>>
    get() {
        return this.members.filterIsInstance<KMutableProperty<*>>()
    }

/**
 * the readOnly Properties
 */
val KClass<*>.readOnlyProperties: List<KProperty<*>>
    get() {
        return this.properties.subtract(this.mutableProperties).toList()
    }

/**
 * cast the kClasses to classes
 */
fun List<KClass<*>>.toClasses(): Array<out Class<*>> {
    return this.map { it.java }.toTypedArray()
}

/**
 * cast the KClasses to classes
 */
fun Array<KClass<*>>.toClasses(): Array<out Class<*>> {
    return this.map { it.java }.toTypedArray()
}

val KParameter.kotlin: KClass<*>
get() = this.type.jvmErasure

val KProperty<*>.returnTypeKotlin: KClass<*>
get() = this.returnType.jvmErasure