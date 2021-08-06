package org.tty.dioc.core.advice

import kotlin.reflect.KClass

/**
 * advice for interface class
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InterfaceAdvice(
    /**
     * the real service type
     */
    val implementationType: KClass<*>
)
