package org.tty.dioc.core.local

import org.tty.dioc.core.ApplicationContext

/**
 * the local component for providing [ApplicationContext]
 */
val LocalContext = ComponentLocal<ApplicationContext>()

/**
 * to resolve the current service
 * @see [LocalContext]
 */
inline fun <reified T: Any> resolve(): T {
    return LocalContext.current().getService(T::class)
}