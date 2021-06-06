package org.tty.dioc.core.declare

import org.tty.dioc.core.lifecycle.LifeCycle

/**
 * to declare this is a service, once the package is under scan region.
 * the service will be auto detected.
 * @see [LifeCycle]
 * @see [org.tty.dioc.core.ApplicationContextBuilder.usePackage]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Service(
    /**
     * the lifecycle of the service.
     * @see [LifeCycle]
     */
    val lifecycle: LifeCycle = LifeCycle.Singleton,
    /**
     * the the service is the lazy service.
     * the service is lazy by default.
     */
    val lazy: Boolean = true
)
