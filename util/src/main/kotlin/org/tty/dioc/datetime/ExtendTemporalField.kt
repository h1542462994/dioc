package org.tty.dioc.datetime

import java.time.DateTimeException
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.*

enum class ExtendTemporalField(
    private val b: TemporalUnit,
    private val r: TemporalUnit
): TemporalField {

    NANO_OF_EPOCH(ChronoUnit.NANOS, ChronoUnit.FOREVER)
    ;

    override fun getBaseUnit(): TemporalUnit {
        return b
    }

    override fun getRangeUnit(): TemporalUnit {
        return r
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
        throw DateTimeException("not support.")
    }

    override fun getFrom(temporal: TemporalAccessor?): Long {
        require(temporal != null)
        if (b.duration < Duration.ofDays(1) && r.duration > Duration.ofDays(1)) {
            // mill of epoch
            if (r == ChronoUnit.FOREVER) {
                val days = temporal.getLong(ChronoField.EPOCH_DAY)

            }
        }
        TODO("not implemented yet.")
    }

    override fun <R : Temporal?> adjustInto(temporal: R, newValue: Long): R {
        throw DateTimeException("not support.")
    }

}