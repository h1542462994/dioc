package org.tty.dioc.util.test

import java.util.*
import kotlin.collections.HashSet

class CollectionTest {
    fun testSet() {
        val set = Collections.synchronizedSet(HashSet<Int>())
    }
}