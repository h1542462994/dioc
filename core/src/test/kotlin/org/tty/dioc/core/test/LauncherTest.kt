package org.tty.dioc.core.test

import org.junit.jupiter.api.Test
import org.tty.dioc.core.launcher.runKernel
import org.tty.dioc.core.launcher.startKernel

class LauncherTest {
    @Test
    fun test() {
        val applicationContext = startKernel()
        println(applicationContext)
    }
}