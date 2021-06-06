package org.tty.dioc.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.LocalApplicationContext
import org.tty.dioc.core.getService
import org.tty.dioc.test.services.HelloService1
import org.tty.dioc.test.services.HelloService2Print
import org.tty.dioc.test.services.PrintService1

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LocalApplicationContextTest {
    /**
     * singleton [HelloService1]
     */
    @Order(1)
    @Test
    @DisplayName("测试singleton服务的正确性")
    fun testOneSingleton() {
        // get the helloService1
        val helloService1: HelloService1 = context.getService()

        assertEquals("hello", helloService1.hello())
    }

    /**
     * 2 * singleton [HelloService1],[PrintService1]
     */
    @Order(2)
    @Test
    @DisplayName("测试singleton->singleton服务的正确性")
    fun testSingletonToSingleton() {
        // get the printService1
        val printService1: PrintService1 = context.getService()

        assertEquals("print:hello",printService1.print())
    }

    /**
     * 2 * singleton [HelloService2Print],[PrintService1]
     */
    @Order(3)
    @Test
    @DisplayName("测试两个singleton通过属性互相注入的正确性")
    fun testCircleDependencySingletonByProperty() {
        val helloService2Print: HelloService2Print = context.getService()

        assertEquals("2:print:hello", helloService2Print.printHello())

        val printService1: PrintService1 = context.getService()

        assertEquals("2:print:hello", printService1.print2())
    }


    companion object {
        private lateinit var context: ApplicationContext

        @BeforeAll
        @JvmStatic
        fun initialize() {
            context = LocalApplicationContext("org.tty.dioc.test.services")
        }
    }
}