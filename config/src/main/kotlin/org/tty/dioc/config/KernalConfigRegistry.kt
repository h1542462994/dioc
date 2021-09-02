package org.tty.dioc.config

import org.tty.dioc.config.basic.ConfigMode
import org.tty.dioc.config.internal.RootApplicationConfig
import org.tty.dioc.config.keys.ConfigKeys
import org.tty.dioc.config.keys.PathSchema
import org.tty.dioc.config.keys.ProviderKeySchema
import org.tty.dioc.config.keys.SimpleDataSchema

/**
 * [ConfigKeys] : org.tty.dioc.config
 */
internal const val configKey = "org.tty.dioc.config.provider"
internal const val configModeKey = "org.tty.dioc.config.mode"
internal const val configModeAnnotationKey = "org.tty.dioc.config.mode.annotation"

val ConfigKeys.config: ProviderKeySchema get() =
    config(ProviderKeySchema(configKey, ApplicationConfig::class, RootApplicationConfig::class, mutable = false))

val ConfigKeys.configMode: SimpleDataSchema<ConfigMode> get() =
    config(SimpleDataSchema(configModeKey, ConfigMode::class, ConfigMode(), false))

val ConfigKeys.configModeAnnotation: PathSchema<Boolean> get() =
    config(PathSchema(configModeAnnotationKey, configModeKey))