package org.tty.dioc.config

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.Once
import org.tty.dioc.config.bean.ConfigMode
import org.tty.dioc.config.internal.BasicApplicationConfig
import org.tty.dioc.config.module.Module
import org.tty.dioc.config.schema.*

internal const val configKey = "org.tty.dioc.config.provider"
internal const val configModeKey = "org.tty.dioc.config.mode"

internal val configSchema = providerSchema<ApplicationConfig>(configKey, listOf(BasicApplicationConfig::class))
internal val configModeSchema = dataSchema(configModeKey, ConfigMode())
internal val configModeAnnotationSchema: PathSchema<Boolean> = configModeSchema pathTo "annotation"
internal val configModeFileSchema: PathSchema<Boolean> = configModeSchema pathTo "file"
internal val configModeTestLengthSchema: PathSchema<String> = configModeSchema pathTo "test.length"

@InternalComponent
class ConfigModule(
    private val configSchemas: ConfigSchemas
): Module {
    @Once
    override fun onInit() {
        configSchemas.config(configSchema)
        configSchemas.config(configModeSchema)
        configSchemas.config(configModeAnnotationSchema)
        configSchemas.config(configModeFileSchema)
        configSchemas.config(configModeTestLengthSchema)
    }
}