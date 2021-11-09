package org.tty.dioc.core.test

import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.useAnnotation
import org.tty.dioc.core.ApplicationEntryPoint
import org.tty.dioc.core.addTransient
import org.tty.dioc.core.declare.ComponentDeclareAware

class TestApplicationEntryPoint: ApplicationEntryPoint {
    override fun onConfiguration(applicationConfig: ApplicationConfig) {
        //applicationConfig.useAnnotation = false
    }

    override fun onStartUp(componentDeclareAware: ComponentDeclareAware) {

    }
}