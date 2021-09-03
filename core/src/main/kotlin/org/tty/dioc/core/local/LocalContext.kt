package org.tty.dioc.core.local

import org.tty.dioc.core.Application
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.ConfigurableApplicationContext

/**
 * the local component to provide [ApplicationContext]
 */
val LocalContext = ComponentLocal<ApplicationContext>()

/**
 * resolve the service on [LocalContext]
 */
inline fun <reified T: Any> resolve(): T {
    return LocalContext.current().getService(T::class)
}

inline fun <reified T: Application> runApplication(): ApplicationContext {
    return ConfigurableApplicationContext()
}