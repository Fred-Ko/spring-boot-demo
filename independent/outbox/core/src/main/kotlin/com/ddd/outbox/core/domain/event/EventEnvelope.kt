package com.ddd.outbox.core.domain.event

import java.time.LocalDateTime
import java.util.*

data class EventEnvelope<T>(
    val id: UUID,
    val aggregateType: String,
    val aggregateId: String,
    val eventType: String,
    val version: Long,
    val timestamp: LocalDateTime,
    val payload: T
) {
    companion object {
        fun <T> from(event: OutboxEvent, payload: T): EventEnvelope<T> {
            return EventEnvelope(
                id = event.id,
                aggregateType = event.aggregateType,
                aggregateId = event.aggregateId,
                eventType = event.eventType,
                version = event.version,
                timestamp = event.createdAt,
                payload = payload
            )
        }
    }
} 