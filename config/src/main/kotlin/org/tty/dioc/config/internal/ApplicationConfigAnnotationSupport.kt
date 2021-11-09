package org.tty.dioc.config.internal

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.ConfigSchema

class ApplicationConfigAnnotationSupport: ApplicationConfig {
    override fun <T : Any> get(configSchema: ConfigSchema<T>): Any {
        TODO("Not yet implemented")
    }

    override fun <T : Any> set(configSchema: ConfigSchema<T>, value: Any) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getList(configSchema: ConfigSchema<T>): List<*> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> setList(configSchema: ConfigSchema<T>, list: List<*>) {
        TODO("Not yet implemented")
    }
}