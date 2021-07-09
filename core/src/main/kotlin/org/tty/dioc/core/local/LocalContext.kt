package org.tty.dioc.core.local

import org.tty.dioc.core.ApplicationContext

val LocalContext = ComponentLocal<ApplicationContext>()

inline fun <reified T: Any> resolve(): T {
    return LocalContext.current().getService(T::class)
}