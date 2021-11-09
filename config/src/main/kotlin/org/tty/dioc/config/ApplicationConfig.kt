package org.tty.dioc.config

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.NoInfer
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi
import org.tty.dioc.config.schema.ConfigSchema
import kotlin.reflect.KClass

@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Declare)
interface ApplicationConfig {
    operator fun <T: Any> get(configSchema: ConfigSchema<T>): Any
    operator fun <T: Any> set(configSchema: ConfigSchema<T>, value: Any)
    fun <T: Any> getList(configSchema: ConfigSchema<T>): List<*>
    fun <T: Any> setList(configSchema: ConfigSchema<T>, list: List<*>)
}

