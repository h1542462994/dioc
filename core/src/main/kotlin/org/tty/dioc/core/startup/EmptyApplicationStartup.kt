package org.tty.dioc.core.startup

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.core.startup.ApplicationStartup
import org.tty.dioc.core.CoreModule.Companion.rootPackageNameSchema
import org.tty.dioc.core.basic.ComponentDeclareAware

/**
 * empty implement for [ApplicationStartup]
 */
class EmptyApplicationStartup(private val rootPackageName: String = ""): ApplicationStartup {
    override fun onConfiguration(config: ApplicationConfig) {
        config[rootPackageNameSchema] = rootPackageName
    }

    override fun onStartUp(aware: ComponentDeclareAware) {

    }

}
