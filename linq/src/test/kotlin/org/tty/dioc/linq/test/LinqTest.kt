package org.tty.dioc.linq.test

import org.junit.jupiter.api.Test
import org.tty.dioc.base.pair
import org.tty.dioc.linq.Linq
import org.tty.dioc.linq.extension.*
import org.tty.dioc.reflect.virtual.operator.*

class LinqTest {
    @Test
    fun testBase() {
        val i = Linq.start<Int>()
        val result = (
                from(i) of 0..9 where (i eq 2) select i
                )

        val result2 = (
                from(i) of 0..10 where (!(i gt 2) or (i le 5)) select i
                )

        val result3 = (
                from(i) of 0..9 where (i within 1..5) select i
                )
    }

    @Test
    fun testCondition() {
        val i = Linq.start<Int>()
        val result = (
                from(i) of 0..9 where (i gt 2) and (i le 5) select (i + 1)
                )

        val result2 = (
                from(i) of 0..9 where (i gt 2) and (i le 5) select pair(i, i + 2)
                )

    }

    @Test
    fun testMultiFrom() {
        val i = Linq.start<Int>()
        val j = Linq.start<Int>()
        val result = (
                from(i) of 0..9
                from(j) of 0..10
                where (i gt 4) and (j gt 5)
                select pair(i, j)
                )
    }
}

