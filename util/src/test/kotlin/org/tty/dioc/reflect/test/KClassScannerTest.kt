package org.tty.dioc.reflect.test

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.tty.dioc.reflect.KClassScanner

class KClassScannerTest {

    @Test
    fun testScan() {
        val scanner = KClassScanner(
            "org.tty.dioc.util",
            recursive = true,
            { true },
            { true }
        )

        /**
         * it will include lambda parsed annotation class
         * and remove compiled class.
         */
        val classes = scanner.doScanAllClasses()
        classes.forEach {
            println(it)
        }
        assertTrue(classes.isNotEmpty())
    }
}