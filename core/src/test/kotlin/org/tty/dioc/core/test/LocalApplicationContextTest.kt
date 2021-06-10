package org.tty.dioc.core.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.LocalApplicationContext
import org.tty.dioc.core.getService
import org.tty.dioc.core.test.services.*
import org.tty.dioc.core.util.ServiceUtil

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LocalApplicationContextTest {
    @Order(0)
    @Test
    fun testJavaService() {
        val helloJava: HelloJava = context.getService()
        assertEquals("helloJava", helloJava.helloJava())
    }

    /**
     * lazy [HelloService1], [PrintService2]
     */
    @Order(1)
    @Test
    @DisplayName("测试@Lazy(inject)的正确性")
    fun testLazyInject() {
        val printService2: PrintService2 = context.getService()
        val impl = printService2 as PrintService2Impl
        assertTrue(ServiceUtil.detectProxy(impl.helloService1))
    }


    /**
     * singleton [HelloService1]
     */
    @Order(2)
    @Test
    @DisplayName("测试singleton服务的正确性")
    fun testOneSingleton() {
        // get the helloService1
        val helloService1: HelloService1 = context.getService()
        val helloService2: HelloService1 = context.getService()

        assertEquals("hello", helloService1.hello())
        // helloService1 和 helloService2 should be refer equal
        assertSame(helloService1, helloService2)
    }

    /**
     * 2 * singleton [HelloService1],[PrintService1]
     */
    @Order(3)
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
    @Order(4)
    @Test
    @DisplayName("测试两个singleton通过属性互相注入的正确性")
    fun testCircleDependencySingletonByProperty() {
        val helloService2Print: HelloService2Print = context.getService()

        assertEquals("2:print:hello", helloService2Print.printHello())

        val printService1: PrintService1 = context.getService()

        assertEquals("2:print:hello", printService1.print2())
    }

    @Test
    fun testScanUtil() {
        context2 = LocalApplicationContext("org.tty.dioc.util.test")
    }

    companion object {
        private lateinit var context: ApplicationContext
        private lateinit var context2: ApplicationContext

        @BeforeAll
        @JvmStatic
        fun initialize() {
            context = LocalApplicationContext("org.tty.dioc.core.test.services")
        }
    }
}