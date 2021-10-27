package org.tty.dioc.annotation

/**
 * the constructor for injection, if the service has more than one constructor.
 * @see [ComponentDeclare]
 */
@MustBeDocumented
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectConstructor
