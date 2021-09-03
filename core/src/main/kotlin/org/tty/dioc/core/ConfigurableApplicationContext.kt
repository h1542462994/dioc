package org.tty.dioc.core

import org.tty.dioc.ConfigAware
import org.tty.dioc.config.ConfigScope
import org.tty.dioc.core.lifecycle.ScopeAbility
import kotlin.reflect.KClass

/**
 * the configurable application context.
 */
class ConfigurableApplicationContext: ApplicationContext, ConfigAware {


    override fun <T : Any> getService(declareType: KClass<T>): T {
        TODO("Not yet implemented")
    }

    override fun scopeAbility(): ScopeAbility {
        TODO("Not yet implemented")
    }

    override fun onInit() {
        TODO("Not yet implemented")
    }

    override fun configure(action: ConfigScope.() -> Unit) {
        TODO("Not yet implemented")
    }
}