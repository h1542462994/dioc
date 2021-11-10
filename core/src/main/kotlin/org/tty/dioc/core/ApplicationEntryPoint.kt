package org.tty.dioc.core

import org.tty.dioc.annotation.InternalComponent
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.core.declare.ComponentDeclareAware

/**
 * entry point of the application
 */
@InternalComponent
interface ApplicationEntryPoint {
    fun onConfiguration(config: ApplicationConfig)
    fun onStartUp(aware: ComponentDeclareAware)
}

