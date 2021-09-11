package org.tty.dioc.core.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.LocalApplicationContext
import org.tty.dioc.core.declare.Lazy
import org.tty.dioc.core.error.ServiceConstructException
import org.tty.dioc.core.getService
import org.tty.dioc.core.local.ComponentContext
import org.tty.dioc.core.local.resolve
import org.tty.dioc.core.test.model.LogLevel
import org.tty.dioc.core.test.model.LogToken
import org.tty.dioc.core.test.services.*
import org.tty.dioc.core.test.services.circle.*
import org.tty.dioc.core.util.ServiceUtil
import java.lang.reflect.InvocationTargetException

/**
 * to test one implementation [LocalApplicationContext] of [ApplicationContext]
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LocalApplicationContextTest {

//    /**
//     * test service with javaClass.
//     * [TestJava]
//     */
//    @Order(0)
//    @Test
//    @DisplayName("测试Java服务的正确性")
//    fun testJavaService() {
//        val testJava: TestJava = context.getService()
//        assertEquals("helloJava", testJava.helloJava())
//    }

    /**
     * singleton,lazy [HelloService]
     */
    @Order(2)
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
    @Order(3)
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

    fun testNotLazyTransient() {
        // because it will cause the fail on initializing container, so it could not be tested.
    }

    /**
     * test annotation [Lazy]
     * [LazyInjectHelloService] -> (lazy) -> [HelloService]
     */
    @Order(6)
    @Test
    @DisplayName("测试@Lazy(inject) by setter. 的正确性")
    fun testLazyInject() {
        val lazyInjectHelloService: LazyInjectHelloService = context.getService()
        val logger: Logger = context.getService()
        /**
         * 在创建[LazyInjectHelloService]，由于[HelloService]被标记为[Lazy]，所以[HelloService]应当没有被初始化。
         * 因此在logger的顶部应当没有包含[HelloService]创建的信息。
         */
        assertNotEquals(helloServiceLog, logger.top())
        assertEquals("hello", lazyInjectHelloService.lazyHello())
        assertEquals(helloServiceLog, logger.top())
    }

    @Order(7)
    @Test
    @DisplayName("测试@Lazy(inject) by constructor. 的正确性")
    fun testLazyInjectByConstructor() {
        val lazyInjectHelloService: LazyInjectHelloServiceByConstructor = context.getService()
        assertTrue(ServiceUtil.detectProxy(lazyInjectHelloService.helloService))
        assertEquals("hello", lazyInjectHelloService.lazyHello())
    }

    @Order(8)
    @Test
    @DisplayName("(e)测试@Lazy(inject) by constructor. 同时在初始化阶段访问时产生的错误")
    fun testLazyInjectByConstructor2() {
        // invocation target 会返回嵌套两层的错误.
        // InvocationTargetException -> UndeclaredException -> ServiceConstructorException
        val e = assertThrows<InvocationTargetException> {
            context.getService<LazyInjectHelloServiceByConstructor2>()
        }
        assertNotNull(e.cause)
        val real = e.cause!!.cause!!
        assert(real is ServiceConstructException)

    }

    /**
     * singleton [HelloService]
     */
    @Order(11)
    @Test
    @DisplayName("测试singleton服务的正确性")
    fun testOneSingleton() {
        // get the helloService1
        val helloService1: HelloService = context.getService()
        val helloService2: HelloService = context.getService()

        assertEquals("hello", helloService1.hello())
        // helloService1 and helloService2 should be referred equal
        assertSame(helloService1, helloService2)
    }

    /**
     * transient [TransientAddService]
     */
    @Order(14)
    @Test
    @DisplayName("测试transient服务的正确性")
    fun testOneTransient() {
        val transientAddService1: TransientAddService = context.getService()
        val transientAddService2: TransientAddService = context.getService()

        /**
         * the 1 and 2 should be two different instance.
         */
        transientAddService1.add()
        assertEquals(1, transientAddService1.current())
        assertEquals(0, transientAddService2.current())

        assertNotSame(transientAddService1, transientAddService2)
    }



    /**
     * singleton [HelloService] <-- singleton [PrintService]
     */
    @Order(21)
    @Test
    @DisplayName("测试singleton->singleton服务的正确性")
    fun testSingletonToSingleton() {
        // get the printService1
        val printService: PrintService = context.getService()

        assertEquals("print:hello",printService.print())
    }

    /**
     * singleton [HelloServiceToPrint] <-> singleton [PrintService]
     */
    @Order(23)
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

    /**
     * transient [PrintServiceTransient] <-> transient [HelloServiceTransient]
     */
    @Order(24)
    @Test
    @DisplayName("(e)测试两个transient通过属性互相注入导致的错误")
    fun testCircleDependencyTransientByProperty() {
        val e = assertThrows<ServiceConstructException> {
            context.getService<PrintServiceTransient>()
        }
        assertEquals(circleDependencyTransientMessage, e.message)
    }

    /**
     * transient [HelloServiceTS] <-> singleton [PrintServiceTS]
     */
    @Order(25)
    @Test
    @DisplayName("(e)测试transient和singleton通过属性互相注入的正确性")
    fun testCircleDependencyTransientAndSingletonByProperty() {
        // 当访问transient服务时，由于singleton会依赖自己所以会创建失败
        val e = assertThrows<ServiceConstructException> {
            context.getService<HelloServiceTS>()
        }
        assertEquals(circleDependencyTSMessage, e.message)
        // 当访问singleton服务时，不会发生错误
        val printService = context.getService<PrintServiceTS>()
        assertEquals("print:hello", printService.print())

    }

    /**
     * singleton [H1] <-> singleton [P1] by constructor.
     */
    @Order(26)
    @Test
    @DisplayName("(e)测试两个singleton通过构造器循环依赖导致的错误.")
    fun testCircleDependencySingletonByConstructor() {
        val e = assertThrows<ServiceConstructException> {
            context.getService<P1>()
        }
        //e.printStackTrace()
        assertEquals(circleDependency1Message, e.message)
    }

    /**
     * singleton [H2] <->(lazy) singleton [P2] by constructor.
     */
    @Order(30)
    @Test
    @DisplayName("测试两个singleton通过构造器循环依赖，但是其中有@Lazy的正确性.")
    fun testCircleDependencySingletonByConstructorLazy() {
        val logger: Logger = context.getService()
        val p2 = context.getService<P2>()

        assertNotEquals(h2Log, logger.top())
        val s = p2.print()
        assertEquals(h2Log, logger.top())
        assertEquals("print:hello", s)
    }

    /**
     * test [ComponentContext]
     */
    @Order(34)
    @Test
    @DisplayName("测试LocalContext正确性")
    fun testLocalContext() {
        val helloService = resolve<HelloService>()
        assertEquals("hello", helloService.hello())
    }

    companion object {
        private lateinit var context: ApplicationContext
        val helloServiceLog = LogToken(LogLevel.Info, "HelloService", "helloService is created.")
        val helloServiceNotLazyLog = LogToken(LogLevel.Info, "HelloServiceNotLazy", "helloServiceNotLazy is created.")
        val h2Log = LogToken(LogLevel.Info, "H2", "==H2Impl is created.")
        const val circleDependencyTransientMessage = "find a cycle dependency link on transient service, it will cause a dead lock, because dependency link class org.tty.dioc.core.test.services.circle.PrintServiceTransientImpl -> ... -> class org.tty.dioc.core.test.services.circle.PrintServiceTransientImpl"
        const val circleDependencyTSMessage = "find a cycle dependency link on transient service, it will cause a dead lock, because dependency link class org.tty.dioc.core.test.services.circle.HelloServiceTSImpl -> ... -> class org.tty.dioc.core.test.services.circle.HelloServiceTSImpl"
        const val circleDependency1Message = "you want to inject a service not created, it will cause dead lock, because dependency link class org.tty.dioc.core.test.services.circle.P1 -> ... -> class org.tty.dioc.core.test.services.circle.P1"

        @BeforeAll
        @JvmStatic
        fun initialize() {
            context = LocalApplicationContext("org.tty.dioc.core.test.services")
            ComponentContext provides context
            // or you can write will this
//            LocalContext provides HolderCall(Companion) {
//                it.context
//            }

        }
    }
}