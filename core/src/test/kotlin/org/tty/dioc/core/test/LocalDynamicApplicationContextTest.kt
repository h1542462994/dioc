package org.tty.dioc.core.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.core.DynamicApplicationContext
import org.tty.dioc.core.LocalDynamicApplicationContext
import org.tty.dioc.core.addScoped2
import org.tty.dioc.core.addSingleton2
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.error.ServiceDeclarationException
import org.tty.dioc.core.local.LocalContext
import org.tty.dioc.core.local.resolve
import org.tty.dioc.core.test.services.dynamic.*

/**
 * to test one implementation [LocalDynamicApplicationContext] of [DynamicApplicationContext]
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LocalDynamicApplicationContextTest {

    @Test
    @Order(0)
    fun testSingleton() {
        context.addSingleton2<AddService, AddServiceStep1>()
        val addService = resolve<AddService>()
        assertEquals(0, addService.current())
    }

    @Test
    @Order(1)
    fun testRepeatSingletonDeclare() {
        val e = assertThrows<ServiceDeclarationException> {
            context.addSingleton2<AddService, AddServiceStep2>()
        }
        assertEquals(addServiceRedundantMessage, e.message)
    }

    @Test
    @Order(2)
    fun testForceReplaceSingletonToScoped() {
        // you can change the existed declaration in forceReplace.
        assertDoesNotThrow {
            context.forceReplace {
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

    fun testScoped() {

    }

    companion object {
        private lateinit var context: DynamicApplicationContext
        private const val addServiceRedundantMessage = "the declaration of the type class org.tty.dioc.core.test.services.dynamic.AddService is redundant."

        @JvmStatic
        @BeforeAll
        fun initialize() {
            context = LocalDynamicApplicationContext()
            LocalContext provides context
        }
    }

}