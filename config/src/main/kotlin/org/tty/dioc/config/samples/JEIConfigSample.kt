package org.tty.dioc.config.samples

/**
 * **JEI** config samples
 */
class JEIConfigSample {
    fun useDelegateForSchema() {
        // property, type, configSchema
        // var ApplicationConfig.useAnnotation by delegateForSchema<Boolean>(configModeAnnotationSchema)
    }
}