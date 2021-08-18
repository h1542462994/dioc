package org.tty.dioc.datetime

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.toInstant(zoneId: ZoneId): Instant {
    val instant = Clock.system(zoneId).instant()
    val rules = zoneId.rules
    val offset = rules.getOffset(instant)
    return this.toInstant(offset)
}