package org.tty.dioc.core.local

import org.tty.dioc.core.ApplicationContext

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