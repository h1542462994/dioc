package org.tty.dioc.config.internal

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.ConfigSchema
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * property delegate for [ConfigSchema].
 * @see [org.tty.dioc.config.schema.delegateForSchema]
 *
 * @sample [org.tty.dioc.config.samples.JEIConfigSample.useDelegateForSchema]
 */
internal class ApplicationConfigDelegate<T: Any, TR: Any>(
    private val configSchema: ConfigSchema<T>
): ReadWriteProperty<ApplicationConfig, TR> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: ApplicationConfig, property: KProperty<*>): TR {
        return thisRef[configSchema] as TR
    }

    override fun setValue(thisRef: ApplicationConfig, property: KProperty<*>, value: TR) {
        thisRef[configSchema] = value
    }
}