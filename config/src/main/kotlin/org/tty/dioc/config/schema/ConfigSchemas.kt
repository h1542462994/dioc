package org.tty.dioc.config.schema

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.util.formatTable
import org.tty.dioc.util.withLeft
import kotlin.reflect.KClass

/**
 * storage for [ConfigSchema]
 */
@InternalComponent
class ConfigSchemas {
    /**
     * storage for [ConfigSchema]
     */
    private val schemaStore = HashMap<String, ConfigSchema>()

    /**
     * config the [ConfigSchema] and return itself
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

    /**
     * get the defaultProvider from [ConfigSchema]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> getDefaultProvider(type: KClass<T>): ProvidersSchema<T> {
        val providerSchemas = schemaStore.values.filterIsInstance<ProvidersSchema<*>>()
            .filter { it.type == type }
        require(providerSchemas.isNotEmpty()) {
            "providerSchema is not provided"
        }
        require(providerSchemas.size == 1) {
            "provideSchemas has more than one declaration"
        }
        return providerSchemas[0] as ProvidersSchema<T>
    }

    override fun toString(): String {
        return formatTable("${ConfigSchemas::class.simpleName}", schemaStore.values.sortedBy { it.name }, title = listOf("name", "tag", "type", "rule", "info")) {
            listOf(it.name, it.tag, it.type.qualifiedName, it.rule, it.info())
        }.toString()
    }
}