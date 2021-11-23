package org.tty.dioc.core.launcher

import org.tty.dioc.annotation.DebugOnly
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.startup.ApplicationStartup
import org.tty.dioc.core.startup.EmptyApplicationStartup
import org.tty.dioc.core.local.ComponentContext


/**
 * to start the kernel and return the applicationContext
 */
fun startKernel(startup: ApplicationStartup = EmptyApplicationStartup()): ApplicationContext {
    return KernelLoader()
        .setApplicationEntryPoint(startup)
        .load()
}

/**
 * to start a console application.
 */
fun startConsole(): ApplicationContext {
    TODO()
}

/**
 * to start the kernel and bind the [ApplicationContext] to [ComponentContext]
 */
@DebugOnly
fun runKernel(startup: ApplicationStartup = EmptyApplicationStartup()): ApplicationContext {
    val applicationContext = startKernel(startup)
    ComponentContext.provides(applicationContext)
    return applicationContext
}