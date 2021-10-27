package org.tty.dioc.core.launcher

import org.tty.dioc.core.ApplicationContext


/**
 * to run the kernel and return the applicationContext
 */
fun runKernel(): ApplicationContext {
    val kernelLoader = KernelLoader()
    return kernelLoader.load()
}