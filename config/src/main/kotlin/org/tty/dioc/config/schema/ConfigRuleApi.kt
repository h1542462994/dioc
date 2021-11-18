package org.tty.dioc.config.schema

/**
 * used on [ConfigSchema]
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigRuleApi(
    val configRule: ConfigRule
)