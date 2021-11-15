package org.tty.dioc.core.test

import org.junit.jupiter.api.*
import org.tty.dioc.config.ApplicationConfig
import org.tty.dioc.config.ConfigModule
import org.tty.dioc.config.schema.ConfigSchemas
import org.tty.dioc.config.useAnnotation
import org.tty.dioc.config.useFile
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.ApplicationEntryPoint
import org.tty.dioc.core.basic.ComponentStorage
import org.tty.dioc.core.basic.ComponentDeclareAware
import org.tty.dioc.core.basic.ComponentDeclares
import org.tty.dioc.core.basic.getComponent
import org.tty.dioc.core.launcher.startKernel

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LauncherTest {
    @Test
    @Order(800)
    fun testGetConfig() {
        val applicationConfig = applicationContext.getComponent<ApplicationConfig>()
        println(applicationConfig.useAnnotation)
        println(applicationConfig.useFile)
        println(applicationConfig.getList(ConfigModule.configSchema))
    }

    @Test
    @Order(1000)
    fun testSetConfig() {
        val applicationConfig = applicationContext.getComponent<ApplicationConfig>()
        applicationConfig.useAnnotation = false
        applicationConfig.useFile = false
        println(applicationConfig.useAnnotation)
        println(applicationConfig.useFile)
    }

    @Test
    @Order(2000)
    fun testPrintConfigSchemas() {
        println(applicationContext.getComponent<ConfigSchemas>())
    }

    @Test
    @Order(3000)
    fun testPrintComponentStorage() {
        println(applicationContext.getComponent<ComponentStorage>())
    }

    @Test
    @Order(4000)
    fun testComponentDeclares() {
        println(applicationContext.getComponent<ComponentDeclares>())
    }

    companion object {
        lateinit var applicationContext: ApplicationContext

        @JvmStatic
        @BeforeAll
        fun initialize() {
            applicationContext = startKernel(EntryPoint())

        }
    }

    class EntryPoint : ApplicationEntryPoint {
        override fun onConfiguration(config: ApplicationConfig) {

        }

        override fun onStartUp(aware: ComponentDeclareAware) {
        }
    }


}