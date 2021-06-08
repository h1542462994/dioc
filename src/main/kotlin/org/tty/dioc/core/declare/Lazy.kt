package org.tty.dioc.core.declare

/**
 * if a service inject place (property or constructor) is annotated [Lazy],
 * mean the service is only be created once call the service.
 * the service is replaced with proxy object.
 * the scope service is must be lazy.
 * @see [org.tty.dioc.core.lifecycle.ServiceProxyFactory]
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Lazy()
