package com.outbox.core.application.service

import com.outbox.core.domain.event.OutboxEvent

interface OutboxEventPublisher {
    fun publish(event: OutboxEvent)
    fun publishAll(events: List<OutboxEvent>)
} 