package org.tty.dioc.advice

import org.tty.dioc.config.keys.ConfigKeys

/**
 * advice for interface class
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InterfaceAdvice(
    /**
     * the key of the [ConfigKeys]
     */
    val key: String
)
