package org.tty.dioc.config.schema

import org.tty.dioc.annotation.NoInfer
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.internal.ApplicationConfigDelegate
import org.tty.dioc.reflect.getProperty
import org.tty.dioc.reflect.returnTypeKotlin
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

inline fun <reified T: Any> getRuleByTypeAndRule(configRule: ConfigRule): ConfigRule {
    var rule = configRule
    if (rule == ConfigRule.NoAssigned) {
        val ruleApi = T::class.findAnnotation<ConfigRuleApi>()
        requireNotNull(ruleApi)
        require(ruleApi.configRule != ConfigRule.NoAssigned) {
            "configRule couldn't be not assigned."
        }
        rule = ruleApi.configRule
    }
    return rule
}

inline fun <reified T: Any> providerSchema(name: String, providers: List<KClass<out T>>, configRule: ConfigRule = ConfigRule.NoAssigned): ProvidersSchema<T> {
    return ProvidersSchema(name, T::class, providers, getRuleByTypeAndRule<T>(configRule))
}

inline fun <reified T: Any> dataSchema(name: String, default: T, configRule: ConfigRule = ConfigRule.NoAssigned): DataSchema<T> {
    return DataSchema(name, T::class, default, getRuleByTypeAndRule<T>(configRule))
}

@Suppress("UNCHECKED_CAST")
inline infix fun <@NoInfer reified T: Any> ConfigSchema.pathTo(path: String): PathSchema<T> {
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
        requireNotNull(p)
        val ruleApi = p.findAnnotation<ConfigRuleApi>()
        if (ruleApi != null) {
            require(ruleApi.configRule != ConfigRule.NoAssigned) {
                "configRule couldn't be not assigned."
            }
            currentRule = ruleApi.configRule
        }
        currentType = p.returnTypeKotlin
    }

    return PathSchema(relevantPath, currentType as KClass<T>, currentSlot, currentRule)
}

/**
 * create a property delegate by [ApplicationConfigDelegate]
 */
fun <T: Any> delegateForSchema(configSchema: ConfigSchema): ReadWriteProperty<ApplicationConfig, T> {
    return ApplicationConfigDelegate(configSchema)
}