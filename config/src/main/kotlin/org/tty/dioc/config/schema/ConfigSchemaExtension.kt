package org.tty.dioc.config.schema

import org.tty.dioc.reflect.getProperty
import org.tty.dioc.reflect.returnTypeKotlin
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
inline infix fun <reified T: Any> ConfigSchema.pathTo(path: String): PathSchema<T> {
    // length -> file.length
    var relevantPath = path
    var currentSlot = this
    // analyse the pathSchema
    while (currentSlot is PathSchema<*>) {
        val ref = currentSlot.referent
        relevantPath = currentSlot.path + "." + relevantPath
        currentSlot = ref
    }

    // visit seq
    val properties = relevantPath.split(".")
    // if file.length
    var currentRule = currentSlot.rule
    require(currentRule != ConfigRule.NoAssigned) {
        "the root rule could n't be not assigned."
    }
    var currentType = when(currentSlot) {
        is DataSchema<*> -> currentSlot.type
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