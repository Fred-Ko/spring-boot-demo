package com.ddd.outbox.adapter.outbound.event

import com.ddd.outbox.core.application.service.OutboxEventPublisher
import com.ddd.outbox.core.domain.event.EventEnvelope
import com.ddd.outbox.core.domain.event.OutboxEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class DefaultOutboxEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) : OutboxEventPublisher {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun publish(event: OutboxEvent) {
        val envelope = EventEnvelope.from(event, event.payload)
        kafkaTemplate.send(event.aggregateType, envelope)
            .whenComplete { result, ex ->
                when {
                    ex != null -> log.error("Failed to send event: {}", ex.message, ex)
                    else -> log.debug("Successfully sent event: {}", result.recordMetadata)
                }
            }
    }

    override fun publishAll(events: List<OutboxEvent>) {
        events.forEach { publish(it) }
    }
}
