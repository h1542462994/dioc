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

    @Order(0)
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

    @Order(1)
    @Test
    fun testChannelReceiveMany() {
        var d = 0
        val channel = Channels
            .create<Int>()

        channel
            .receive { data, next ->  next(data) }
            .receive { data, _ -> d = data }

        channel.emit(2)
        assertEquals(2, d)

        d = 0
        channel
            .cleanReceivers()
            .receive { _, _ ->
                // intercept the data flow.
            }
            .receive { data, _ -> d = data }

        channel.emit(2)
        assertEquals(0, d)
    }

    @Order(2)
    @Test
    fun testChannelTransfer() {
        var d = 0
        val channel1 = Channels.create<Int>()
        val channel2 = Channels.create<Int>()

        channel1
            .next(channel2)

        channel2.receive { data, _ ->
            d = data
        }

        channel1.emit(2)
        assertEquals(2, d)
    }

    @Order(3)
    @Test
    fun testChannelMap() {
        var d = ""
        val channel = Channels.create<Int>()

        channel
            .map { it.toString() }
            .receive { data, _ ->
                d = data
            }

        channel.emit(4)
        assertEquals("4", d)
    }

    @Order(4)
    @Test
    fun testChannelMapMany() {
        var d = ""
        val channel = Channels.create<Int>()

        channel
            .map { it * it }
            .map { it + 2 }
            .map { it.toString() }
            .receive { data, _ -> d = data }

        channel.emit(4)
        assertEquals("18", d)
    }

    @Order(5)
    @Test
    fun testChannelCombine() {
        var d = 0
        val channel1 = Channels.create<Int>()
        val channel2 = Channels.create<Int>()

        Channels.combine(
            channel1.map { it * it },
            channel2.map { it + 2 }
        ).receive { data, _ -> d += data }



        channel1.emit(2)
        assertEquals(4, d)
        channel2.emit(1)
        assertEquals(7, d)
    }
}