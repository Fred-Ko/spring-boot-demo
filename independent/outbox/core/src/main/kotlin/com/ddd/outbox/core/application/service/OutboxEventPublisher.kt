package com.ddd.outbox.core.application.service

import com.ddd.outbox.core.domain.event.OutboxEvent

interface OutboxEventPublisher {
    fun publish(event: OutboxEvent)
    fun publishAll(events: List<OutboxEvent>)
}