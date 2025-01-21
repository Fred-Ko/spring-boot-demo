package com.ddd.outbox.adapter.outbound.persistence.entity

import com.ddd.outbox.adapter.outbound.persistence.converter.JsonConverter
import java.time.LocalDateTime
import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "outbox_events")
class OutboxEventEntity(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "aggregate_type", nullable = false)
    val aggregateType: String,

    @Column(name = "aggregate_id", nullable = false)
    val aggregateId: String,

    @Column(name = "event_type", nullable = false)
    val eventType: String,

    @Convert(converter = JsonConverter::class)
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    val payload: Any,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "published_at")
    var publishedAt: LocalDateTime?,

    @Column(name= "topic", nullable = false)
    val topic: String,

    @Version
    @Column(name = "version")
    val version: Long
)