package org.tty.dioc.core

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.core.declare.ComponentDeclareAware

/**
 * entry point of the application
 */
interface ApplicationEntryPoint {
    fun onConfiguration(applicationConfig: ApplicationConfig)
    fun onStartUp(componentDeclareAware: ComponentDeclareAware)
}

