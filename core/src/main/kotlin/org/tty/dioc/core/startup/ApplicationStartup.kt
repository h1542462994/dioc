package org.tty.dioc.core.startup

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.schema.ConfigRule
import org.tty.dioc.config.schema.ConfigRuleApi
import org.tty.dioc.core.CoreModule
import org.tty.dioc.core.basic.ComponentDeclareAware
import org.tty.dioc.reflect.packageName
import org.tty.dioc.core.ApplicationContext

/**
 * class for start up [ApplicationContext]
 */
@InternalComponent
@ConfigRuleApi(configRule = ConfigRule.Readonly)
interface ApplicationStartup {
    fun onConfiguration(config: ApplicationConfig) {
        config[CoreModule.rootPackageNameSchema] = this::class.packageName
    }
    fun onStartUp(aware: ComponentDeclareAware)
}

