package org.tty.dioc.config

import org.tty.dioc.annotation.DebugOnly
import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.annotation.Once
import org.tty.dioc.config.bean.ConfigMode
import org.tty.dioc.config.internal.BasicApplicationConfig
import org.tty.dioc.config.module.Module
import org.tty.dioc.config.schema.*

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

    companion object {
        private const val configKey = "org.tty.dioc.config.provider"
        private const val configModeKey = "org.tty.dioc.config.mode"

        val configSchema = providerSchema<ApplicationConfig>(configKey, listOf(BasicApplicationConfig::class))
        val configModeSchema = dataSchema(configModeKey, ConfigMode())
        val configModeAnnotationSchema: PathSchema<Boolean> = configModeSchema pathTo "annotation"
        val configModeFileSchema: PathSchema<Boolean> = configModeSchema pathTo "file"
        @DebugOnly
        val configModeTestLengthSchema: PathSchema<String> = configModeSchema pathTo "test.length"
    }
}