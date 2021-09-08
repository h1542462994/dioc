package org.tty.dioc.core.local

import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.util.Logger
import org.tty.dioc.util.SimpleConsoleLogger

fun <T: Any> staticComponentLocalOf(default: () -> T): ComponentLocal<T> {
    val local = DefaultComponentLocal<T>()
    val slot = 1
    local.provides(slot) {
        default()
    }
    return local
}
fun <T: Any> staticComponentLocalOf(): ComponentLocal<T> {
    return staticComponentLocalOf {
        throw IllegalStateException("component has no value.")
    }
}

/**
 * the local component to provide [ApplicationContext]
 */
val ComponentContext = staticComponentLocalOf<ApplicationContext>()

/**
 * the local component to provide [Logger]
 */
val ComponentLogger = staticComponentLocalOf<Logger> {
    SimpleConsoleLogger()
}

/**
 * resolve the service on [ComponentContext]
 */
inline fun <reified T: Any> resolve(): T {
    return ComponentContext.current.getService(T::class)
}

