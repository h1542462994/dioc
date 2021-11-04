package org.tty.dioc.annotation

/**
 * to declare this is a service, once the package is under scan region.
 * the service will be auto-detected.
 * @see [Lifecycle]
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
