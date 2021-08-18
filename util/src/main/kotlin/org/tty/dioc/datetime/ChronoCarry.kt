package org.tty.dioc.datetime

import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

/**
 * the carry of [baseUnit] and [rangeUnit]
 * to help calculate [ExtendTemporalField]
 */
enum class ChronoCarry(
    val value: Long,
    val baseUnit: TemporalUnit,
    val rangeUnit: TemporalUnit
) {
    INVALID(0L, ChronoUnit.NANOS, ChronoUnit.NANOS),

    /**
     * nano per micro.
     */
    NANO_PER_MICRO(1000L, ChronoUnit.NANOS, ChronoUnit.MICROS),

    /**
     * micro per milli.
     */
    MICRO_PER_MILLI(1000L, ChronoUnit.MICROS, ChronoUnit.MILLIS),

    /**
     * milli per second
     */
    MILLI_PER_SECOND(1000L, ChronoUnit.MILLIS, ChronoUnit.SECONDS),

    /**
     * second per minute
     */
    SECOND_PER_MINUTE(60L, ChronoUnit.SECONDS, ChronoUnit.MINUTES),

    /**
     * minute per second
     */
    MINUTE_PER_HOUR(60L, ChronoUnit.MINUTES, ChronoUnit.HOURS),

    /**
     * hour per day
     */
    HOUR_PER_DAY(24L, ChronoUnit.HOURS, ChronoUnit.DAYS),

    /**
     * nano per second
     */
    NANO_PER_SECOND(NANO_PER_MICRO * MICRO_PER_MILLI * MILLI_PER_SECOND, ChronoUnit.NANOS, ChronoUnit.SECONDS),

    /**
     * micro per second
     */
    MICRO_PER_SECOND(MICRO_PER_MILLI * MILLI_PER_SECOND, ChronoUnit.MICROS, ChronoUnit.SECONDS),

    /**
     * second per day
     */
    SECOND_PER_DAY(SECOND_PER_MINUTE * MINUTE_PER_HOUR * HOUR_PER_DAY, ChronoUnit.SECONDS, ChronoUnit.DAYS),

    /**
     * minute per day
     */
    MINUTE_PER_DAY(MINUTE_PER_HOUR * HOUR_PER_DAY, ChronoUnit.MINUTES, ChronoUnit.DAYS),

    /**
     * nano per day
     */
    NANO_PER_DAY(NANO_PER_SECOND * SECOND_PER_DAY, ChronoUnit.NANOS, ChronoUnit.DAYS),

    /**
     * micro per day
     */
    MICRO_PER_DAY(MICRO_PER_SECOND * SECOND_PER_DAY, ChronoUnit.MICROS, ChronoUnit.DAYS),

    /**
     * milli per day
     */
    MILLI_PER_DAY(MILLI_PER_SECOND * SECOND_PER_DAY, ChronoUnit.MILLIS, ChronoUnit.DAYS),

    /**
     * second per hour
     */
    SECOND_PER_HOUR(SECOND_PER_MINUTE * MINUTE_PER_HOUR, ChronoUnit.SECONDS, ChronoUnit.HOURS),

    /**
     * milli per hour
     */
    MILLI_PER_HOUR(MILLI_PER_SECOND * SECOND_PER_HOUR, ChronoUnit.MILLIS, ChronoUnit.HOURS),

    /**
     * micro per hour
     */
    MICRO_PER_HOUR(MICRO_PER_MILLI * MILLI_PER_HOUR, ChronoUnit.MICROS, ChronoUnit.HOURS),

    /**
     * nano per hour
     */
    NANO_PER_HOUR(NANO_PER_MICRO * MICRO_PER_HOUR, ChronoUnit.NANOS, ChronoUnit.HOURS),

    /**
     * milli per minute
     */
    MILLI_PER_MINUTE(MILLI_PER_SECOND * SECOND_PER_MINUTE, ChronoUnit.MILLIS, ChronoUnit.MINUTES)

    ;

    /**
     * return the [ChronoUnit] which [baseUnit]
     */
    fun toBaseDayField(): ChronoField {
        return when(this) {
            INVALID -> throw IllegalArgumentException("invalid.")
            NANO_PER_DAY -> ChronoField.NANO_OF_DAY
            MICRO_PER_DAY -> ChronoField.MICRO_OF_DAY
            MILLI_PER_DAY -> ChronoField.MILLI_OF_DAY
            SECOND_PER_DAY -> ChronoField.SECOND_OF_DAY
            MINUTE_PER_DAY -> ChronoField.MINUTE_OF_DAY
            HOUR_PER_DAY -> ChronoField.HOUR_OF_DAY
            else -> throw IllegalArgumentException("not support.")
        }
    }

    operator fun times(value: Long): Long {
        return value * this.value
    }

    operator fun times(value: ChronoCarry): Long {
        return value.value * this.value
    }

    operator fun times(value: Int): Int {
        return (value * this.value).toInt()
    }

    companion object {
        fun of(base: TemporalUnit, range: TemporalUnit): ChronoCarry {
            return values().firstOrNull { it.baseUnit == base && it.rangeUnit == range } ?: INVALID
        }
    }
}

private operator fun Long.times(value: ChronoCarry): Long {
    return value.value * this
}

