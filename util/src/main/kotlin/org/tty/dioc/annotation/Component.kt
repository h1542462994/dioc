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
     * name of the component
     */
    val name: String = "",
    /**
     * the lifecycle of the service.
     * @see [Lifecycle]
     */
    val lifecycle: Lifecycle = Lifecycle.Singleton,
    /**
     * **true** means the service will be created unless be used.
     */
    val lazy: Boolean = true
)
