package org.tty.dioc.linq.test

import org.junit.jupiter.api.Test
import org.tty.dioc.linq.Linq
import org.tty.dioc.linq.extension.*

class BaseTest {
    fun testFrom() {
        val i = Linq.start<Int>()
        val t = from(i) of 1..9 where { i > 0 && i < 8 } select i

    }

    @Test
    fun testLambda() {


    }
}