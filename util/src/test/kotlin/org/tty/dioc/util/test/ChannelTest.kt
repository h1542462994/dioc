package org.tty.dioc.util.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.observable.Channel
import org.tty.dioc.observable.Channels
import org.tty.dioc.observable.receive

/**
 * test [Channel]
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ChannelTest {

    @Order(1)
    @Test
    fun testChannelReceive() {

        var d = 0
        val channel = Channels
            .create<Int>()

        channel.receive { data, _ ->
            d = data
        }

        channel.emit(1)
        assertEquals(1, d)
    }





}