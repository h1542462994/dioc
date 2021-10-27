package org.tty.dioc.config.schema

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.util.withLeft
import kotlin.reflect.KClass

/**
 * storage for schema
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
        val space = 5

        val nameLength = schemaStore.values.maxOf { it.name.length } + space
        val tagLength = schemaStore.values.maxOf { it.tag.length } + space
        val typeLength = schemaStore.values.maxOf { it.type.qualifiedName!!.length } + space
        val configRuleLength = ConfigRule.values().maxOf { it.name.length } + space
        val infoLength = schemaStore.values.maxOf { it.info().length } + space

        fun generateTitle(): String {
            return withLeft("", space) +
                    withLeft("name", nameLength) +
                    withLeft("tag", tagLength) +
                    withLeft("type", typeLength) +
                    withLeft("rule", configRuleLength) +
                    withLeft("info", infoLength)
        }

        fun generateLine(configSchema: ConfigSchema): String {
            return withLeft("", space) +
                    withLeft(configSchema.name, nameLength) +
                    withLeft(configSchema.tag, tagLength) +
                    withLeft(configSchema.type.qualifiedName, typeLength) +
                    withLeft(configSchema.rule.name, configRuleLength) +
                    withLeft(configSchema.info(), infoLength)
        }

        return "configSchemas [\n" +
                generateTitle() + "\n" +
                schemaStore.values.sortedBy { it.name }.joinToString("\n") { generateLine(it) } +
                "\n]"
    }
}