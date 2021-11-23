package org.tty.dioc.core.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.startup.ApplicationStartup
import org.tty.dioc.core.basic.*
import org.tty.dioc.core.launcher.runKernel
import org.tty.dioc.core.local.resolve
import org.tty.dioc.core.test.model.LogLevel
import org.tty.dioc.core.test.model.LogToken
import org.tty.dioc.core.test.services.Logger
import org.tty.dioc.core.test.services.dynamic.AddService
import org.tty.dioc.core.test.services.dynamic.AddServiceStep1
import org.tty.dioc.core.test.services.dynamic.AddServiceStep2
import org.tty.dioc.error.ServiceConstructException
import org.tty.dioc.error.ServiceDeclarationException

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LocalDynamicApplicationContextTest {

    @Test
    @Order(0)
    fun testSingleton() {
        aware.addSingleton2<AddService, AddServiceStep1>()
        val addService = resolve<AddService>()
        assertEquals(0, addService.current())
    }

    @Test
    @Order(1)
    fun testRepeatSingletonDeclare() {
        val e = assertThrows<ServiceDeclarationException> {
            aware.addSingleton2<AddService, AddServiceStep2>()
        }
        assertEquals(addServiceRedundantMessage, e.message)
    }

    @Test
    @Order(2)
    fun testForceReplaceSingletonToScoped() {
        // you can change the existed declaration in forceReplace.
        assertDoesNotThrow {
            aware.forceReplace {
                it.addScoped2<AddService, AddServiceStep2>()
            }
        }

        // use the service within scope.
        context.withScope {
            val addService = resolve<AddService>()
            assertEquals(0, addService.current())
            addService.add()
            assertEquals(2, addService.current())
        }

        // attempt to get a service out of a scope, then thrown.
        assertThrows<ServiceConstructException> {
            resolve<AddService>()
        }
    }

    @Test
    @Order(3)
    fun testScoped() {
        aware.forceReplace {
            it.addScoped2<AddService, AddServiceStep2>(lazy = false)
        }

        context.withScope {
            val logger = resolve<Logger>()
            assertEquals(log2, logger.top())
            aware.forceReplace {
                it.addScoped2<AddService, AddServiceStep1>(lazy = false)
                assertEquals(log1, logger.top())
            }

        }
    }

    companion object {
        private lateinit var context: ApplicationContext
        private lateinit var aware: ComponentDeclareAware
        private const val addServiceRedundantMessage = "the declaration of the type class org.tty.dioc.core.test.services.dynamic.AddService is redundant."
        private val log1 = LogToken(LogLevel.Debug, "AddServiceStep1", "init")
        private val log2 = LogToken(LogLevel.Debug, "AddServiceStep2", "init")

        @JvmStatic
        @BeforeAll
        fun initialize() {
            context = runKernel(StartUp())
            aware = context.getComponent<ComponentDeclares>()
        }

    }

    class StartUp: ApplicationStartup {
        override fun onStartUp(aware: ComponentDeclareAware) {

        }
    }

}