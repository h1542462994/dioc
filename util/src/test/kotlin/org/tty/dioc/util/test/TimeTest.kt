package org.tty.dioc.util.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.*
import java.util.*


class TimeTest {
    @Test
    fun testNow() {
        // Old Apis
        val oldDate = Date()
        println(oldDate)
        val oldDate2 = Calendar.getInstance().time
        println(oldDate2)

        // New Apis
        val instant = Instant.now()
        println(instant)

        val localDateTime = LocalDateTime.now()
        println(localDateTime)

        val zonedDateTime = Instant.now().atZone(ZoneId.systemDefault())
        println(zonedDateTime)
    }

    @Test
    fun testSpecific() {
        // 首先固定一个时钟 对应本地时间2021/7/12 3:00
        val fixedInstant = Instant.parse("2021-07-11T19:00:00.00Z")
        val fixedClock = Clock.fixed(
            fixedInstant, ZoneId.systemDefault()
        )

        val date = Date(fixedClock.millis())
        println(date)
        val date2 = Calendar.getInstance().apply {
            this.timeInMillis = fixedClock.millis()
        }.time
        println(date2)
        // 特别需要注意：Calendar.getInstance()会获取本地日历系统，设置的时间也是按照本地时间来设置的。
        val date3 = Calendar.getInstance().apply {
            this.set(Calendar.YEAR, 2021)
            // 特别需要注意的是，Calendar中Month范围为0~11.
            this.set(Calendar.MONTH, Calendar.JULY)
            this.set(Calendar.DATE, 12)
            this.set(Calendar.HOUR, 3)
            this.set(Calendar.MINUTE, 0)
            this.set(Calendar.SECOND, 0)
            // 这个一定要设置成0
            this.set(Calendar.MILLISECOND, 0)
        }.time
        println(date3)

        val date4 = Instant.from(fixedInstant)
        val date5 = Instant.ofEpochMilli(fixedClock.millis())
        println(date4)
        println(date5)

        val date6 = LocalDateTime.of(2021,7,12,3,0,0)
        println(date6)

        val date7 = ZonedDateTime.ofInstant(date4, ZoneId.systemDefault())
        println(date7)
    }

    @Test
    fun testZoneIds() {
        // 0时区
        println(ZoneId.of("Z"))
        println(ZoneOffset.UTC)
        assertEquals(ZoneId.of("Z"), ZoneOffset.UTC)

        // UTC时区，需要注意的是ZoneId.of("UTC") != ZoneOffset.UTC的
        println(ZoneId.of("UTC"))
        println(ZoneId.of("GMT"))

        // 系统默认时区，一般为Asia/Shanghai
        println(ZoneId.systemDefault())

        // UTC+8时区，下面四个方法是等价的
        println(ZoneId.of("+8"))
        println(ZoneId.of("+08:00"))
        println(ZoneOffset.of("+08:00"))
        println(ZoneOffset.ofHours(8))

        // Asia/Shanghai时区
        println(ZoneId.of("Asia/Shanghai"))
    }

    @Test
    fun testInstantTransform() {
        // transform from date to instant.
        val instant = Date().toInstant()
        println(instant)

        // the standard way to get instant
        val instant1 = Instant.now()
        println(instant1)

        // the standard way to get localDateTime
        val localDateTime = LocalDateTime.now()
        println(localDateTime)

        val zonedDateTime = instant1.atZone(ZoneId.systemDefault())
        println(zonedDateTime)
        val localDateTime2 = zonedDateTime.toLocalDateTime()
        println(localDateTime2)

        val zonedDateTime2 = localDateTime.atZone(ZoneId.systemDefault())
        println(zonedDateTime2)
    }

    @Test
    fun testChange() {
        val date1 = LocalDate.of(2021,8,31)
        println(date1)
        val date2 = date1.plusMonths(1)
        println(date2)
        val date3 = date2.plusMonths(1)
        println(date3)
    }

    @Test
    fun testDayOfWeek() {
        for(dayOfWeek in DayOfWeek.values()) {
            println("$dayOfWeek - ${dayOfWeek.value}")
        }
    }
}

