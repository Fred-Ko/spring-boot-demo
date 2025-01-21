package com.ddd.outbox.core.domain.repository

import com.ddd.outbox.core.domain.event.OutboxEvent

interface OutboxEventRepository {
    fun save(event: OutboxEvent): OutboxEvent
    fun findUnpublishedEvents(): List<OutboxEvent>
}