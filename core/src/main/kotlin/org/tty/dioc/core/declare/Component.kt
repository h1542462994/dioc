package org.tty.dioc.core.declare

/**
 * to declare this is a service, once the package is under scan region.
 * the service will be auto-detected.
 * @see [Lifecycle]
 * @see [org.tty.dioc.core.ApplicationContextBuilder.usePackage]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Component(
    /**
     * the lifecycle of the service.
     * @see [Lifecycle]
     */
    val lifecycle: Lifecycle = Lifecycle.Singleton,
    /**
     * if true, the service will be created on boot.
     */
    val lazy: Boolean = true
)
