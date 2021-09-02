package org.tty.dioc.core

import org.tty.dioc.core.lifecycle.ScopeAbility
import kotlin.reflect.KClass

/**
 * the configurable application context.
 */
class ConfigurableApplicationContext: ApplicationContext {


    override fun <T : Any> getService(declareType: KClass<T>): T {
        TODO("Not yet implemented")
    }

    override fun scopeAbility(): ScopeAbility {
        TODO("Not yet implemented")
    }
}