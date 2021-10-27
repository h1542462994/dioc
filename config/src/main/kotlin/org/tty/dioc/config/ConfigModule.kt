package org.tty.dioc.config

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.bean.ConfigMode
import org.tty.dioc.config.internal.BasicApplicationConfig
import org.tty.dioc.config.module.Module
import org.tty.dioc.config.schema.*

internal const val configKey = "org.tty.dioc.config.provider"
internal val configSchema = ProvidersSchema(configKey, ApplicationConfig::class,
    default = listOf(BasicApplicationConfig::class),
    rule = ConfigRule.Declare)
internal const val configModeKey = "org.tty.dioc.config.mode"
internal val configModeSchema = DataSchema(configModeKey, ConfigMode::class, ConfigMode(annotation = true, file = true),
    rule = ConfigRule.CodeReadOnly
)
internal val configModeAnnotationSchema = PathSchema<Boolean>("$configModeKey.annotation", configModeKey)
internal val configModeFileSchema = PathSchema<Boolean>("$configModeKey.file", configModeKey)

@InternalComponent
class ConfigModule(
    private val configSchemas: ConfigSchemas
): Module {
    override fun initialize() {
        configSchemas.config(configSchema)
        configSchemas.config(configModeSchema)
        configSchemas.config(configModeAnnotationSchema)
        configSchemas.config(configModeFileSchema)
    }
}