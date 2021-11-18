package org.tty.dioc.config.internal

import org.tty.dioc.base.InitSuperComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.*
import org.tty.dioc.error.unSupported
import org.tty.dioc.reflect.getWithPropertyChain
import org.tty.dioc.reflect.setWithPropertyChain
import kotlin.reflect.full.hasAnnotation

/**
 * runtime support for [ApplicationConfig]
 */
class ApplicationConfigRuntimeSupport : ApplicationConfig, InitSuperComponent<ApplicationConfig>{
    /**
     * storage. *Type NoInfer*
     */
    private val storage = HashMap<ConfigSchema<*>, Any>()

    /**
     * the superComponent
     */
    private lateinit var superComponent: ApplicationConfig

    override fun <T : Any> get(configSchema: ConfigSchema<T>): Any {
        return when(configSchema) {
            is DataSchema<*> -> {
                getFromStorage(configSchema)
            }
            is PathSchema<*> -> {
                val chain = configSchema.name.substring(configSchema.referent.name.length + 1)
                get(configSchema.referent).getWithPropertyChain(chain)!!
            }
            is ProvidersSchema<*> -> {
                getFromStorage(configSchema)
            }
            else -> unSupported("unreachable code.")
        }
    }



    override fun <T : Any> set(configSchema: ConfigSchema<T>, value: Any) {
        require(configSchema.rule != ConfigRule.Declare) {
            "the declare configRule couldn't be assigned in runtime."
        }

        when(configSchema) {
            is DataSchema<*> -> {
                storage[configSchema] = value
            }
            is PathSchema<*> -> {
                val chain = configSchema.name.substring(configSchema.referent.name.length + 1)
                get(configSchema.referent).setWithPropertyChain(chain, value)
            }
            is ProvidersSchema<*> -> {
                storage[configSchema] = listOf(value)
            }
        }
    }

    override fun <T : Any> getList(configSchema: ConfigSchema<T>): List<*> {
        require(configSchema::class.hasAnnotation<ConfigListable>()) {
            "only configListable can getList"
        }
        return when(configSchema) {
            is ProvidersSchema<*> -> {
                getListFromStorage(configSchema)
            }
            else -> unSupported("unreachable code.")
        }
    }

    override fun <T : Any> setList(configSchema: ConfigSchema<T>, list: List<*>) {
        require(configSchema.rule != ConfigRule.Declare) {
            "the declare configRule couldn't be assigned in runtime."
        }
        require(configSchema::class.hasAnnotation<ConfigListable>()) {
            "only configListable can setList"
        }
        when(configSchema) {
            is ProvidersSchema<*> -> {
                storage[configSchema] = list
            }
            else -> unSupported("un reachable code.")
        }
    }

    override fun initSuper(superComponent: ApplicationConfig) {
        this.superComponent = superComponent
    }

    private fun getFromStorage(configSchema: ConfigSchema<*>): Any {
        if (!storage.containsKey(configSchema)) {
            storage[configSchema] = superComponent[configSchema]
        }

        return storage.getValue(configSchema)
    }

    private fun getListFromStorage(configSchema: ConfigSchema<*>): List<*> {
        if (!storage.containsKey(configSchema)) {
            storage[configSchema] = superComponent.getList(configSchema)
        }

        return storage.getValue(configSchema) as List<*>
    }

}