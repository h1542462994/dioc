package org.tty.dioc.config.schema

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigRuleApi(
    val configRule: ConfigRule
)