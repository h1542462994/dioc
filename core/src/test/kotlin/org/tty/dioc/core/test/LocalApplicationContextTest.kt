package org.tty.dioc.core.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.LocalApplicationContext
import org.tty.dioc.core.getService
import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.test.model.LogLevel
import org.tty.dioc.core.test.model.LogToken
import org.tty.dioc.core.test.services.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LocalApplicationContextTest {

    /**
     * test service with javaClass.
     * [TestJava]
     */
    @Order(0)
    @Test
    @DisplayName("测试Java服务的正确性")
    fun testJavaService() {
        val testJava: TestJava = context.getService()
        assertEquals("helloJava", testJava.helloJava())
    }

    /**
     * singleton,lazy [HelloService]
     */
    @Order(1)
    @Test
    @DisplayName("测试@Service(lazy = true)的正确性")
    fun testLazySingleton() {
        val logger: Logger = context.getService()
        /**
         * 当没有使用[HelloService]时，[HelloServiceImpl.onInit]方法应当不会被调用。
         */
        assertNotEquals(helloServiceLog, logger.top())
    }

    /**
     * singleton,no lazy [HelloServiceNotLazy]
     */
    @Order(2)
    @Test
    @DisplayName("测试@Service(lazy = false)的正确性")
    fun testNotLazy() {
        val logger: Logger = context.getService()
        /**
         * 由于[HelloServiceNotLazy]被标识为@Service(lazy = false)，所以[HelloServiceNotLazy]应当立即被加载，
         * 所以在[Logger]中会有启动日志，即使没有被使用到
         */
        assertEquals(helloServiceNotLazyLog, logger.top())
    }

    /**
     * test annotation [Lazy]
     * [LazyInjectHelloService] -> (lazy) -> [HelloService]
     */
    @Order(3)
    @Test
    @DisplayName("测试@Lazy(inject)的正确性")
    fun testLazyInject() {
        val lazyInjectHelloService: LazyInjectHelloService = context.getService()
        val logger: Logger = context.getService()
        /**
         * 在创建[LazyInjectHelloService]，由于[HelloService]被标记为[Lazy]，所以[HelloService]应当没有被初始化。
         */
        assertNotEquals(helloServiceLog, logger.top())
        assertEquals("hello", lazyInjectHelloService.lazyHello())
        assertEquals(helloServiceLog, logger.top())
    }


    /**
     * singleton [HelloService]
     */
    @Order(4)
    @Test
    @DisplayName("测试singleton服务的正确性")
    fun testOneSingleton() {
        // get the helloService1
        val helloService1: HelloService = context.getService()
        val helloService2: HelloService = context.getService()

        assertEquals("hello", helloService1.hello())
        // helloService1 和 helloService2 should be refer equal
        assertSame(helloService1, helloService2)
    }

    /**
     * transient [HelloService]
     */
    @Order(5)
    @Test
    @DisplayName("测试transient服务的正确性")
    fun testOneTransient() {
        val transientAddService1: TransientAddService = context.getService()
        val transientAddService2: TransientAddService = context.getService()

        transientAddService1.add()
        assertEquals(1, transientAddService1.current())
        assertEquals(0, transientAddService2.current())

        assertNotSame(transientAddService1, transientAddService2)
    }



    /**
     * 2 * singleton [HelloService],[PrintService]
     */
    @Order(9)
    @Test
    @DisplayName("测试singleton->singleton服务的正确性")
    fun testSingletonToSingleton() {
        // get the printService1
        val printService: PrintService = context.getService()

        assertEquals("print:hello",printService.print())
    }

    /**
     * 2 * singleton [HelloServiceNotLazy],[PrintService]
     */
    @Order(10)
    @Test
    @DisplayName("测试两个singleton通过属性互相注入的正确性")
    fun testCircleDependencySingletonByProperty() {
        val helloServiceToPrint: HelloServiceToPrint = context.getService()
        val printService: PrintService = context.getService()
        if (helloServiceToPrint is HelloServiceToPrintImpl && printService is PrintServiceImpl) {
            assertSame(helloServiceToPrint.printService, printService)
            assertSame(printService.helloServiceToPrint, helloServiceToPrint)
            assertEquals( "print:hello", helloServiceToPrint.print())
        } else {
            throw AssertionError("not real class")
        }
    }

    @Order(11)
    @Test
    @DisplayName("测试两个transient通过属性互相注入导致的错误")
    fun testCircleDependencyTransientByProperty() {
        val printServiceTransient: PrintServiceTransient = context.getService()

    }

    @Test
    fun testScanUtil() {
        context2 = LocalApplicationContext("org.tty.dioc.util.test")
    }

    companion object {
        private lateinit var context: ApplicationContext
        private lateinit var context2: ApplicationContext
        val helloServiceLog = LogToken(LogLevel.Info, "HelloService", "helloService is created.")
        val helloServiceNotLazyLog = LogToken(LogLevel.Info, "HelloServiceNotLazy", "helloServiceNotLazy is created.")

        @BeforeAll
        @JvmStatic
        fun initialize() {
            context = LocalApplicationContext("org.tty.dioc.core.test.services")
        }
    }
}