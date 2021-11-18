package org.tty.dioc.config

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.schema.*

/**
 * configuration, provide dynamic config interaction.
 * @see [ConfigSchema]
 */
@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Declare)
interface ApplicationConfig {
    /**
     * get the value, or get the only one list value.
     * if you want to get the config by name, you should use [autoSchema]
     * @sample [org.tty.dioc.config.samples.JEIConfigSample.useApplicationConfigGet]
     */
    operator fun <T: Any> get(configSchema: ConfigSchema<T>): Any

    /**
     * set the value, or set the only one list value.if you want to set the config by name, you should use [autoSchema]
     */
    operator fun <T: Any> set(configSchema: ConfigSchema<T>, value: Any)

    /**
     * get the list value, only annotated [ConfigListable] can use this function.
     */
    fun <T: Any> getList(configSchema: ConfigSchema<T>): List<*>

    /**
     * set the list value, only annotated [ConfigListable] can use this function.
     */
    fun <T: Any> setList(configSchema: ConfigSchema<T>, list: List<*>)


}

