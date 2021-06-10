package org.tty.dioc.util

import java.lang.reflect.Constructor
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

@Deprecated("you should call constructor.create(*args) instead.")
fun Constructor<*>.create(args: List<Any>): Any {
    return when {
        args.isEmpty() -> {
            this.newInstance()
        }
        args.size == 1 -> {
            this.newInstance(args[0])
        }
        args.size == 2 -> {
            this.newInstance(args[0], args[1])
        }
        args.size == 3 -> {
            this.newInstance(args[0], args[1], args[2])
        }
        args.size == 4 -> {
            this.newInstance(args[0], args[1], args[2], args[3])
        }
        args.size == 5 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4])
        }
        args.size == 6 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5])
        }
        args.size == 7 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }
        args.size == 8 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }
        args.size == 9 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])
        }
        args.size == 10 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])
        }
        args.size == 11 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10])
        }
        args.size == 12 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11])
        }
        args.size == 13 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12])
        }
        args.size == 14 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13])
        }
        args.size == 15 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14])
        }
        args.size == 16 -> {
            this.newInstance(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15])
        }
        else -> throw IllegalStateException("too many arguments.")
    }
}

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