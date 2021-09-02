package org.tty.dioc.config

import org.tty.dioc.config.internal.RootApplicationConfig
import org.tty.dioc.config.keys.ConfigKeys
import org.tty.dioc.config.keys.ProviderKeySchema

/**
 * [ConfigKeys] : org.tty.dioc.config
 */
internal const val configKey = "org.tty.dioc.config"

val ConfigKeys.config: ProviderKeySchema get() =
    config(ProviderKeySchema(configKey, ApplicationConfig::class, RootApplicationConfig::class, mutable = false))