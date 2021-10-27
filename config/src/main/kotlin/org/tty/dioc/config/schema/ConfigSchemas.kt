package org.tty.dioc.config.schema

/**
 * the visitor for [ConfigSchema]
 */
class ConfigSchemas {
    /**
     * the store saving [ConfigSchema]
     */
    private val schemaStore = HashMap<String, ConfigSchema>()

    /**
     * config the [ConfigSchema]
     */
    fun <T: ConfigSchema> config(value: T): T {
        schemaStore[value.name] = value
        return value
    }

    /**
     * get the [ConfigSchema] by [name]
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T: ConfigSchema> get(name: String): T? {
        val value = schemaStore[name]

        return if (value == null) {
            null
        } else {
            value as T
        }
    }

}