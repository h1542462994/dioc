package org.tty.dioc.core.test

import org.junit.jupiter.api.Test
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.bean.ConfigMode
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.useAnnotation
import org.tty.dioc.config.useFile
import org.tty.dioc.core.getComponent
import org.tty.dioc.core.launcher.startKernel

class LauncherTest {
    @Test
    fun test() {
        val applicationContext = startKernel()
        val applicationConfig = applicationContext.getComponent<ApplicationConfig>()
        println(applicationConfig.useAnnotation)
        println(applicationConfig.useFile)
        println(applicationConfig.get<ConfigMode>("org.tty.dioc.config.mode").test)
        println(applicationContext)
        val configSchemas = applicationContext.getComponent<ConfigSchemas>()
        println(configSchemas)
    }
}