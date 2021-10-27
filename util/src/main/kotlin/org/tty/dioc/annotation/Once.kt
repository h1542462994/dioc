package org.tty.dioc.annotation

/**
 * to declare a method could only be called once.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Once
