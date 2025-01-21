package com.ddd.outbox.core.application.service

import com.ddd.outbox.core.domain.event.OutboxEvent
import com.ddd.outbox.core.domain.repository.OutboxEventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OutboxService(
    private val outboxEventRepository: OutboxEventRepository,
    private val eventPublisher: OutboxEventPublisher
) {
    @Transactional
    fun saveEvent(
        aggregateType: String,
        aggregateId: String,
        eventType: String,
        version: Long,
        topic: String,
        payload: Any
    ): OutboxEvent {
        val event = OutboxEvent.create(
            aggregateType = aggregateType,
            aggregateId = aggregateId,
            eventType = eventType,
            version = version,
            topic = topic,
            payload = payload
        )
        return outboxEventRepository.save(event)
    }

    @Transactional
    fun publishPendingEvents() {
        val unpublishedEvents = outboxEventRepository.findUnpublishedEvents()
        unpublishedEvents.forEach { event ->
            try {
                eventPublisher.publish(event)
                event.markAsPublished()
                outboxEventRepository.save(event)
            } catch (e: Exception) {
                // 실패한 이벤트는 다음 배치에서 재시도
                throw e
            }
        }
    }
}