package com.ddd.outbox.adapter.outbound.event

import com.ddd.outbox.core.application.service.OutboxEventPublisher
import com.ddd.outbox.core.domain.event.OutboxEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class DefaultOutboxEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : OutboxEventPublisher {
    override fun publish(event: OutboxEvent) {
        applicationEventPublisher.publishEvent(event)
    }

    override fun publishAll(events: List<OutboxEvent>) {
        events.forEach { publish(it) }
    }
}