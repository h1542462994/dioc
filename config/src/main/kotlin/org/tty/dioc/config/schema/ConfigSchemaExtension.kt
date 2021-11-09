package org.tty.dioc.config.schema

import org.tty.dioc.annotation.NoInfer
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.internal.ApplicationConfigDelegate
import org.tty.dioc.error.notProvided
import org.tty.dioc.reflect.getProperty
import org.tty.dioc.reflect.returnTypeKotlin
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

inline fun <reified T: Any> getRuleByType(configRule: ConfigRule): ConfigRule {
    var rule = configRule
    if (rule == ConfigRule.NoAssigned) {
        val ruleApi = T::class.findAnnotation<ConfigRuleApi>()
        requireNotNull(ruleApi) {
            "configRule is null"
        }
        require(ruleApi.configRule != ConfigRule.NoAssigned) {
            "configRule couldn't be not assigned."
        }
        rule = ruleApi.configRule
    }
    return rule
}

inline fun <reified T: Any> providerSchema(name: String, providers: List<KClass<out T>>, configRule: ConfigRule = ConfigRule.NoAssigned): ProvidersSchema<T> {
    return ProvidersSchema(name, T::class, providers, getRuleByType<T>(configRule))
}

inline fun <reified T: Any> dataSchema(name: String, default: T, configRule: ConfigRule = ConfigRule.NoAssigned): DataSchema<T> {
    return DataSchema(name, T::class, default, getRuleByType<T>(configRule))
}

@Suppress("UNCHECKED_CAST")
fun <@NoInfer T: Any> ConfigSchema<*>.pathTo(path: String, resultType: KClass<T>? = null): PathSchema<T> {
    // relevant visit path to root.
    var relevantPath = path
    // currentSlot to visit.
    var currentSlot = this
    // analyse the pathSchema
    while (currentSlot is PathSchema<*>) {
        val ref = currentSlot.referent
        relevantPath = currentSlot.path + "." + relevantPath
        currentSlot = ref
    }

    // property visit chain
    val properties = relevantPath.split(".")

    // rule
    var currentRule = currentSlot.rule
    require(currentRule != ConfigRule.NoAssigned) {
        "the root rule could n't be not assigned."
    }
    var currentType = when(currentSlot) {
        is DataSchema<*> -> currentSlot.type
        // pathSchema is only valid to pathSchema/dataSchema
        else -> error("the root schema must be dataSchema")
    }
    for (visit in properties) {
        val p = currentType.getProperty<KProperty<*>>(visit)
        requireNotNull(p) {
            "the property for pathSchema is not found."
        }
        val ruleApi = p.findAnnotation<ConfigRuleApi>()
        if (ruleApi != null) {
            require(ruleApi.configRule != ConfigRule.NoAssigned) {
                "configRule couldn't be no assigned."
            }
            currentRule = ruleApi.configRule
        }
        currentType = p.returnTypeKotlin
    }

    if (resultType != null) {
        require(currentType == resultType) {
            "result type is not equal to $resultType"
        }
    }

    return PathSchema(relevantPath, currentType as KClass<T>, currentSlot, currentRule)
}

inline infix fun <reified T: Any> ConfigSchema<*>.pathTo(path: String): PathSchema<T> {
    return pathTo(path, T::class)
}

fun <T: Any> autoPathSchema(configSchemas: ConfigSchemas, name: String): PathSchema<T>? {
    // name like org.tty.dioc.config.mode.text
    val tokens = name.split(".")
    for (i in 1 until tokens.size) {
        val left = tokens.slice(0 until tokens.size - i).joinToString(".")
        val right = tokens.slice(tokens.size - i until tokens.size).joinToString(".")
        val leftSchema = configSchemas.get<Any>(left)
        if (leftSchema != null) {
            // not use type check.
            return leftSchema.pathTo(right, resultType = null)
        }
    }
    return null
}

fun <T: Any> autoSchema(configSchemas: ConfigSchemas, name: String): ConfigSchema<T>? {
    var configSchema: ConfigSchema<T>? = configSchemas[name]
    if (configSchema == null) {
        configSchema = autoPathSchema(configSchemas, name)
    }
    return configSchema
}

/**
 * create a property delegate by [ApplicationConfigDelegate]
 */
fun <@NoInfer T: Any> delegateForSchema(configSchema: ConfigSchema<T>): ReadWriteProperty<ApplicationConfig, T> {
    return ApplicationConfigDelegate(configSchema)
}

fun <T: Any, @NoInfer TR: Any> delegateForSchema2(configSchema: ConfigSchema<T>): ReadWriteProperty<ApplicationConfig, TR> {
    return ApplicationConfigDelegate(configSchema)
}