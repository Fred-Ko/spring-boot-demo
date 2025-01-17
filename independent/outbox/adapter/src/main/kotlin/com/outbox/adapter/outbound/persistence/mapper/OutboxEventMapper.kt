package com.outbox.adapter.outbound.persistence.mapper

import com.outbox.adapter.outbound.persistence.entity.OutboxEventEntity
import com.outbox.core.domain.event.OutboxEvent

object OutboxEventMapper {
    fun toEntity(event: OutboxEvent): OutboxEventEntity {
        return OutboxEventEntity(
            id = event.id,
            aggregateType = event.aggregateType,
            aggregateId = event.aggregateId,
            eventType = event.eventType,
            payload = event.payload,
            createdAt = event.createdAt,
            publishedAt = event.publishedAt,
            version = event.version
        )
    }

    fun toDomain(entity: OutboxEventEntity): OutboxEvent {
        return OutboxEvent.create(
            aggregateType = entity.aggregateType,
            aggregateId = entity.aggregateId,
            eventType = entity.eventType,
            payload = entity.payload
        ).also {
            // 리플렉션을 사용하여 private 필드 설정
            it::class.java.getDeclaredField("id").apply {
                isAccessible = true
                set(it, entity.id)
            }
            it::class.java.getDeclaredField("createdAt").apply {
                isAccessible = true
                set(it, entity.createdAt)
            }
            it::class.java.getDeclaredField("publishedAt").apply {
                isAccessible = true
                set(it, entity.publishedAt)
            }
            it::class.java.getDeclaredField("version").apply {
                isAccessible = true
                set(it, entity.version)
            }
        }
    }
} 