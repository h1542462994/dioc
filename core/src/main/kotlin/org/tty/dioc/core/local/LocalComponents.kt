package org.tty.dioc.core.local

import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.util.Logger
import org.tty.dioc.util.SimpleConsoleLogger

inline fun <reified T: Any> staticComponentLocalOf(crossinline default: () -> T): ComponentLocal<T> {
    val local = DefaultComponentLocal<T>()
    val slot = 1
    local.provides(slot) {
        default()
    }
    return local
}
inline fun <reified T: Any> staticComponentLocalOf(): ComponentLocal<T> {
    return staticComponentLocalOf {
        throw IllegalStateException("component has no value.")
    }
}

/**
 * the local component to provide [ApplicationContext]
 */
val LocalContext = staticComponentLocalOf<ApplicationContext>()

val LocalLogger = staticComponentLocalOf<Logger> {
    SimpleConsoleLogger()
}

/**
 * resolve the service on [LocalContext]
 */
inline fun <reified T: Any> resolve(): T {
    return LocalContext.current.getService(T::class)
}

