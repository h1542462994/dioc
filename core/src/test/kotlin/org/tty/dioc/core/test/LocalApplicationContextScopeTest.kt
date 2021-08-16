package org.tty.dioc.core.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.LocalApplicationContext
import org.tty.dioc.core.getService
import org.tty.dioc.core.lifecycle.Scope
import org.tty.dioc.core.lifecycle.ScopeAbility
import org.tty.dioc.core.test.services.scope.ScopedAddService
import kotlin.concurrent.thread

/**
 * to test the [ScopeAbility] for [ApplicationContext]
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LocalApplicationContextScopeTest {
    /**
     * test declaration of the scope
     */
    @Order(0)
    @DisplayName("测试Scope声明的正确性")
    @Test
    fun testScopeDeclare() {
        var scope = context.currentScope()
        assertNull(scope)
        scope = context.beginScope()
        assertSame(scope, context.currentScope())
        context.endScope()
        assertNull(context.currentScope())
    }

    /**
     * test the embed declare of the scope
     */
    @Order(4)
    @DisplayName("测试Scope声明的正确性(Embed)")
    @Test
    fun testScopeEmbedDeclare() {
        val scope1: Scope = context.beginScope()
        assertSame(scope1, context.currentScope())
        val scope2: Scope = context.beginScope()
        assertSame(scope2, context.currentScope())
        context.endScope()
        assertSame(scope1, context.currentScope())
    }

    /**
     * test the embed withScope
     */
    @Order(8)
    @DisplayName("测试withScope声明的正确性(Embed)")
    @Test
    fun testWithScopeEmbedDeclare() {
        context.withScope { it1 ->
            context.withScope { it2 ->
                assertSame(it2, context.currentScope())
            }
            assertSame(it1, context.currentScope())
        }
    }

    /**
     * scope is not equal in difference thread.
     */
    @Order(12)
    @DisplayName("测试Scope在不同线程中不共享")
    @Test
    fun testWithDifferentThread() {
        var scope: Scope? = null

        thread {
            scope = context.beginScope()
        }.join()

        thread {
            assertNotSame(scope, context.currentScope())
        }
    }

    @Order(16)
    @DisplayName("测试withScope的正确性(Embed)")
    @Test
    fun testWithScopeEmbed() {
        context.withScope {
            val addService = context.getService<ScopedAddService>()
            addService.add()
            context.withScope {
                val addService1 = context.getService<ScopedAddService>()
                assertNotSame(addService, addService1)
                assertEquals(0, addService1.current())
            }
            assertEquals(1, context.getService<ScopedAddService>().current())
        }
    }



    companion object {
        private lateinit var context: ApplicationContext

        @JvmStatic
        @BeforeAll
        fun initialize(){
            context = LocalApplicationContext("org.tty.dioc.core.test.services")
        }
    }
}