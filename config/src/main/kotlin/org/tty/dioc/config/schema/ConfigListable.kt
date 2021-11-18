package org.tty.dioc.config.schema

import org.tty.dioc.config.ApplicationConfig

/**
 * listable [ConfigSchema]
 * @see [ApplicationConfig.getList]
 * @see [ApplicationConfig.setList]
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigListable
