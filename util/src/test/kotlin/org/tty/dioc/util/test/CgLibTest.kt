package org.tty.dioc.util.test

import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import org.junit.jupiter.api.Test
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashSet
import kotlin.time.ExperimentalTime
import kotlin.time.days

class CgLibTest {
    @Test
    fun testAOPInt() {
        val enhancer = Enhancer()
        enhancer.setSuperclass(Int::class.java)
        enhancer.setCallback(object: MethodInterceptor {
            override fun intercept(obj: Any?, method: Method?, args: Array<out Any>?, proxy: MethodProxy?): Any {
                println("call before.")
                require(proxy != null)
                val result = proxy.invokeSuper(obj, args)
                println("call after.")
                return result
            }
        })
        val value = enhancer.create() as Int
        println(value.toString())

        val student = Student()
        with(student) {
            val x = ::id
        }


    }

    class Student {
        var id: String = ""
        var name: String = ""
    }

}