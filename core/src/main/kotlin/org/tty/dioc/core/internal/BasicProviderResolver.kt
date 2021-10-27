package org.tty.dioc.core.internal

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.base.DisplayString
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ProvidersSchema
import org.tty.dioc.core.basic.BasicComponentStorage
import org.tty.dioc.core.basic.ProviderResolver
import org.tty.dioc.core.launcher.configSchemas
import org.tty.dioc.error.NotProvidedException
import org.tty.dioc.error.notProvided
import org.tty.dioc.reflect.kotlin
import org.tty.dioc.reflect.toClasses
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.kotlinFunction

/**
 * create a combined provider based on [ProvidersSchema]
 * the resolver will only be valid on [ConfigRule.Declare]
 */
@InternalComponent
class BasicProviderResolver(
    private val componentStorage: BasicComponentStorage
): ProviderResolver {
    override fun <T : Any> resolveProvider(name: String): T {
        val configSchema = componentStorage.configSchemas.get<ProvidersSchema<T>>(name)
        require(configSchema != null) {
            "configSchema is not defined."
        }
        require(configSchema.rule == ConfigRule.Declare) {
            "basicProviderResolver could only resolve the $configSchema which has the rule declare."
        }
        val interfaceType = configSchema.type
        val providerTypes = configSchema.default
        val providers = providerTypes.map {
            createProvider(it)
        }
        //componentStorage.addComponent(name, combinedProviderProxy)
        return createProxy(interfaceType, providers)
    }

    private fun <T: Any> createProvider(type: KClass<T>): T {
        val constructors = type.constructors
        require(constructors.size == 1) {
            "provider $type has none or more than 1 constructor(s). so we couldn't which constructor to call."
        }
        val constructor = constructors.first()


        val parameterMap = constructor.parameters.associateWith {
            val parameterType = it.kotlin
            componentStorage.getComponent(parameterType)
        }
        return constructor.callBy(parameterMap)
    }

    /**
     * create the proxy for provider.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T: Any> createProxy(interfaceType: KClass<T>, providers: List<T>): T {
        val proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), listOf(interfaceType).toClasses())
        @kotlin.jvm.Throws(InvocationTargetException::class)
        { _, method, args ->
            var resultValue: Any? = null
            var assigned = false
            providers.forEach {
                try {
                    resultValue = method.invoke(it, *args)
                    assigned = true
                } catch (e: InvocationTargetException) {
                    if (e.cause is NotProvidedException) {
                        // ignore if the cause is not provided exception
                    } else {
                        throw e
                    }
                }
            }
            if (!assigned) {
                notProvided("value is not provided")
            }
            resultValue
        }
        return proxy as T
    }
}