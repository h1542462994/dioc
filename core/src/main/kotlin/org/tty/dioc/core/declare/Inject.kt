package org.tty.dioc.core.declare

/**
 * to declare the field in service should be injected
 * @see [ServiceDeclare]
 */
@MustBeDocumented
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
