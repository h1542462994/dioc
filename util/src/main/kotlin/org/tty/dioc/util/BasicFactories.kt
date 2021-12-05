package org.tty.dioc.util

import org.tty.dioc.error.notProvided
import java.lang.reflect.Proxy

inline fun <reified T> factoryOfNotProvided(message: String): T {
    val proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), arrayOf(T::class.java)
    ) { _, _, _ -> notProvided(message) }
    return proxy as T
}