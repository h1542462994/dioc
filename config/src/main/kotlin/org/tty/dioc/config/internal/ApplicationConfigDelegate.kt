package org.tty.dioc.config.internal

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.ConfigSchema
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * property delegate for [ConfigSchema]
 */
internal class ApplicationConfigDelegate<T: Any>(
    private val configSchema: ConfigSchema
): ReadWriteProperty<ApplicationConfig, T> {
    override fun getValue(thisRef: ApplicationConfig, property: KProperty<*>): T {
        return thisRef[configSchema]
    }

    override fun setValue(thisRef: ApplicationConfig, property: KProperty<*>, value: T) {
        thisRef[configSchema] = value
    }
}