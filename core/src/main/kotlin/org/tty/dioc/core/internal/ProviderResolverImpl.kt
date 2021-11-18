package org.tty.dioc.core.internal

import org.tty.dioc.base.InitSuperComponent
import org.tty.dioc.config.ConfigModule
import org.tty.dioc.config.schema.ProvidersSchema
import org.tty.dioc.config.schema.autoSchema
import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.core.basic.ProviderResolver
import org.tty.dioc.core.basic.findInternalComponent
import org.tty.dioc.core.launcher.configSchemas
import org.tty.dioc.core.launcher.logger
import org.tty.dioc.error.NotProvidedException
import org.tty.dioc.error.notProvided
import org.tty.dioc.reflect.kotlin
import org.tty.dioc.reflect.toClasses
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

/**
 * create a combined provider based on [ProvidersSchema]
 */
internal class ProviderResolverImpl(
    private val componentStorage: ComponentStorage
): ProviderResolver {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> resolveProvider(name: String): T {
        val configSchema = autoSchema<T>(componentStorage.configSchemas, name)

        require(configSchema != null) {
            "configSchema is not defined."
        }
        // warning: T is not checked.
        configSchema as ProvidersSchema<T>

        val interfaceType = configSchema.type

        var providerTypes = configSchema.default

        val applicationConfig = componentStorage.findInternalComponent(ConfigModule.configSchema)
        if (applicationConfig != null) {
            providerTypes = applicationConfig.getList(configSchema) as List<KClass<out T>>
        }

        var superComponent: T? = null

        val providers = providerTypes.map {
            superComponent = createProvider(it, superComponent)
            superComponent as T
        }
        //componentStorage.addComponent(name, combinedProviderProxy)
        require(providers.isNotEmpty()) {
            "no provider(s) found"
        }

        return if (providers.size > 1) {
            // more than 1 providers will create a proxy that reuse the provider
            createProxy(interfaceType, providers)
        } else {
            // else return the real provider
            providers[0]
        }

    }



    private fun <T: Any> createProvider(type: KClass<T>, superComponent: Any?): T {
        val constructors = type.constructors
        require(constructors.size == 1) {
            "provider $type has none or more than 1 constructor(s). so we couldn't which constructor to call."
        }
        val constructor = constructors.first()

        val parameterMap = constructor.parameters.associateWith {
            val parameterType = it.kotlin
            componentStorage.findComponent(parameterType)
        }
        val component = constructor.callBy(parameterMap)

        if (component is InitSuperComponent<*> && superComponent != null) {
            @Suppress("UNCHECKED_CAST")
            (component as InitSuperComponent<T>).initSuper(superComponent as T)
        }

        return component
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
                    resultValue = if (args == null) {
                        method.invoke(it)
                    } else {
                        method.invoke(it, *args)
                    }
                    assigned = true
                } catch (e: InvocationTargetException) {
                    if (e.cause is NotProvidedException) {
                        // ignore if the cause is not provided exception
                        componentStorage.logger.e("BasicProviderResolver", e.cause!!.message)
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