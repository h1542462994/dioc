package org.tty.dioc.core.launcher

import org.tty.dioc.annotation.DebugOnly
import org.tty.dioc.core.ApplicationContext
import org.tty.dioc.core.ApplicationEntryPoint
import org.tty.dioc.core.local.ComponentContext


/**
 * to start the kernel and return the applicationContext
 */
fun startKernel(entryPoint: ApplicationEntryPoint): ApplicationContext {
    return KernelLoader()
        .setApplicationEntryPoint(entryPoint)
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
fun runKernel(entryPoint: ApplicationEntryPoint) {
    ComponentContext.provides(startKernel(entryPoint))
}