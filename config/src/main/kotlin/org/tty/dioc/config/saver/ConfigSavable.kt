package org.tty.dioc.config.saver

/**
 * a savable config.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigSavable(
    val configSaver: String = "default:file=name"
)
