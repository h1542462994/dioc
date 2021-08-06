package org.tty.dioc.core.declare

/**
 * to declare this is a service, once the package is under scan region.
 * the service will be auto-detected.
 * @see [Lifecycle]
 * @see [org.tty.dioc.core.ApplicationContextBuilder.usePackage]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Service(
    /**
     * the lifecycle of the service.
     * @see [Lifecycle]
     */
    val lifecycle: Lifecycle = Lifecycle.Singleton,
    /**
     * the the service is the lazy service.
     * the service is lazy by default.
     */
    val lazy: Boolean = true
)
