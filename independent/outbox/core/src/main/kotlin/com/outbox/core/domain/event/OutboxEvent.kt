package com.outbox.core.domain.event

import java.time.LocalDateTime
import java.util.*

class OutboxEvent private constructor(
    val id: UUID,
    val aggregateType: String,
    val aggregateId: String,
    val eventType: String,
    val payload: Any,
    val createdAt: LocalDateTime,
    var publishedAt: LocalDateTime?,
    val version: Long
) {
    fun markAsPublished() {
        publishedAt = LocalDateTime.now()
    }

    companion object {
        fun create(
            aggregateType: String,
            aggregateId: String,
            eventType: String,
            payload: Any
        ): OutboxEvent {
            return OutboxEvent(
                id = UUID.randomUUID(),
                aggregateType = aggregateType,
                aggregateId = aggregateId,
                eventType = eventType,
                payload = payload,
                createdAt = LocalDateTime.now(),
                publishedAt = null,
                version = 0
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OutboxEvent

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
} 