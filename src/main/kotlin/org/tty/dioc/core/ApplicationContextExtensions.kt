package org.tty.dioc.core

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> ApplicationContext.getService(): T {
    val type: Class<T> = T::class.java
    return this.getService(type)
}