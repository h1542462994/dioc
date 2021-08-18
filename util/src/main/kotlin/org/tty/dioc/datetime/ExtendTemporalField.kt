package org.tty.dioc.datetime

import java.time.DateTimeException
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.*

enum class ExtendTemporalField(
    private val base: TemporalUnit,
    private val range: TemporalUnit
): TemporalField {

    NANO_OF_EPOCH(ChronoUnit.NANOS, ChronoUnit.FOREVER),
    MILLI_OF_EPOCH(ChronoUnit.MILLIS, ChronoUnit.FOREVER)

    ;

    override fun getBaseUnit(): TemporalUnit {
        return base
    }

    override fun getRangeUnit(): TemporalUnit {
        return range
    }

    override fun range(): ValueRange {
        return ValueRange.of(0, Long.MAX_VALUE)
    }

    override fun isDateBased(): Boolean {
        return false
    }

    override fun isTimeBased(): Boolean {
        return false
    }

    override fun isSupportedBy(temporal: TemporalAccessor?): Boolean {
        if (temporal is LocalDateTime) {
            return true
        }
        return false
    }

    override fun rangeRefinedBy(temporal: TemporalAccessor?): ValueRange {
        // ignore the check of the value range.
        // only support getLong()
        return ValueRange.of(0, Long.MAX_VALUE)
    }

    override fun getFrom(temporal: TemporalAccessor?): Long {
        require(temporal != null)
        if (base.duration < Duration.ofDays(1) && range.duration > Duration.ofDays(1)) {
            // mill of epoch
            if (range == ChronoUnit.FOREVER) {
                val days = temporal.getLong(ChronoField.EPOCH_DAY)
                val carry = ChronoCarry.of(base, ChronoUnit.DAYS)
                val epochTick = carry * days + temporal.getLong(carry.toBaseDayField())
            }


        }
        throw IllegalStateException("not support.")
    }

    override fun <R : Temporal?> adjustInto(temporal: R, newValue: Long): R {
        throw DateTimeException("not support.")
    }

}