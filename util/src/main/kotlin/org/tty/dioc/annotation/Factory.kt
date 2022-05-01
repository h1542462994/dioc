package org.tty.dioc.annotation

import kotlin.reflect.KClass

/**
 * declare the component should be created by factory.
 * if the annotation is on class, inject of the component will be redirected to the factory.
 * if the annotation is on property
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Factory(val factoryBuilder: KClass<*>)
