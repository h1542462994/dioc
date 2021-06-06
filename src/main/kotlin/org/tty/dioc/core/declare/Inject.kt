package org.tty.dioc.core.declare

/**
 * to declare the field in service should be injected
 * @see [Service]
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject()
