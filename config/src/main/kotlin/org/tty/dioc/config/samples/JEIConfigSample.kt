package org.tty.dioc.config.samples

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.schema.autoSchema
import org.tty.dioc.error.unSupported

/**
 * **JEI** config samples
 */
class JEIConfigSample {
    private fun <T> slot(): T {
        unSupported("slot value")
    }

    fun useDelegateForSchema() {
        // property, type, configSchema
        // var ApplicationConfig.useAnnotation by delegateForSchema<Boolean>(configModeAnnotationSchema)
    }

    @Suppress("UNUSED_VARIABLE")
    fun useApplicationConfigGet() {
        val configSchemas = slot<ConfigSchemas>()
        val applicationConfig = slot<ApplicationConfig>()

        val schema = autoSchema<Int>(configSchemas,"a.b.c.x")
        if (schema != null) {
            val value = applicationConfig[schema]
            // do something.
        }
    }
}