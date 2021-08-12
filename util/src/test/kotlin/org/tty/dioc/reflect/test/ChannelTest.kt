package org.tty.dioc.reflect.test

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.tty.dioc.observable.*

/**
 * test [Channel]
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ChannelTest {

    @Order(0)
    @Test
    fun testChannelIntercept() {

        var d = 0
        val channel = Channels
            .create<Int>()

        channel.intercept { data, _ ->
            d = data
        }

        channel.emit(1)
        assertEquals(1, d)
    }

    @Order(1)
    @Test
    fun testChannelObserve() {
        var d = 0
        val channel = Channels
            .create<Int>()

        channel.observe { data ->
            d = data
        }

        channel.emit(1)
        assertEquals(1, d)

    }

    @Order(2)
    @Test
    fun testChannelInterceptMany() {
        var d = 0
        val channel = Channels
            .create<Int>()

        channel
            .intercept { data, next ->  next(data) }
            .intercept { data, next -> next(data) }
            .intercept { data, _ -> d = data }

        channel.emit(2)
        assertEquals(2, d)

        d = 0
        channel
            .cleanInterceptors()
            .intercept { _, _ ->
                // intercept the data flow.
            }
            .intercept { data, next -> next(data) }
            .intercept { data, _ -> d = data }

        channel.emit(2)
        assertEquals(0, d)
    }

    @Order(3)
    @Test
    fun testChannelTransfer() {
        var d = 0
        val channel1 = Channels.create<Int>()
        val channel2 = Channels.create<Int>()

        channel1
            .next(channel2)

        channel2.observe { data -> d = data }

        channel1.emit(2)
        assertEquals(2, d)
    }

    @Order(4)
    @Test
    fun testChannelMap() {
        var d = ""
        val channel = Channels.create<Int>()

        channel
            .map { it.toString() }
            .observe { data -> d = data }

        channel.emit(4)
        assertEquals("4", d)
    }

    @Order(5)
    @Test
    fun testChannelMapMany() {
        var d = ""
        val channel = Channels.create<Int>()

        channel
            .map { it * it }
            .map { it + 2 }
            .map { it.toString() }
            .observe { data -> d = data }

        channel.emit(4)
        assertEquals("18", d)
    }

    @Order(6)
    @Test
    fun testChannelCombine() {
        var d = 0
        val channel1 = Channels.create<Int>()
        val channel2 = Channels.create<Int>()

        Channels.combine(
            channel1.map { it * it },
            channel2.map { it + 2 }
        ).intercept { data, _ -> d += data }



        channel1.emit(2)
        assertEquals(4, d)
        channel2.emit(1)
        assertEquals(7, d)
    }

    @Order(7)
    @Test
    fun testChannelSync() {
        var result1 = 0
        var result2 = ""
        var result3 = ""
        val channel1 = Channels.create<Int>()
        val channel2 = Channels.create<Int>()
        val channel3 = Channels.create<String>()

        Channels
            .sync(channel1, channel2)
            .map { it.sum() }
            .observe { result1 = it }

        Channels
            .sync(channel1, channel3)
            .map { "${it.first},${it.second}" }
            .observe { result2 = it }

        Channels
            .sync(channel1, channel2, channel3)
            .map { "${it.first},${it.second},${it.third}" }
            .observe { result3 = it }

        channel1.emit(4)
        assertEquals(0, result1)
        assertEquals("", result2)
        assertEquals("", result3)

        channel2.emit(5)
        assertEquals(9, result1)
        assertEquals("", result2)
        assertEquals("", result3)

        channel3.emit("X")
        assertEquals(9, result1)
        assertEquals("4,X", result2)
        assertEquals("4,5,X", result3)
    }

    @Order(8)
    @Test
    fun testChannelRecord() {
        var result1 = 0
        var result2 = ""
        var result3 = ""
        val channel1 = Channels.create<Int>()
        val channel2 = Channels.create<Int>()
        val channel3 = Channels.create<String>()


        Channels
            .record(channel1, channel2)
            .init(0, 0)
            .map { it.sum() }
            .observe { result1 = it }

        Channels
            .record(channel1, channel3)
            .init(0, "")
            .map { "${it.first},${it.second}" }
            .observe { result2 = it }

        Channels
            .record(channel1, channel2, channel3)
            .init(0, 0, "")
            .map { "${it.first},${it.second},${it.third}" }
            .observe { result3 = it }
    }
}