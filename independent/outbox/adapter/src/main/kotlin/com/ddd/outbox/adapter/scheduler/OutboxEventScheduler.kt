package com.ddd.outbox.adapter.scheduler

import com.ddd.outbox.core.application.service.OutboxService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxEventScheduler(
    private val outboxService: OutboxService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelayString = "\${outbox.scheduler.fixed-delay:5000}")
    fun publishPendingEvents() {
        log.info("Start publishing pending outbox events")
        try {
            val publishedCount = outboxService.publishPendingEvents()
            log.info("Successfully published {} pending outbox events", publishedCount)
        } catch (e: Exception) {
            log.error("Failed to publish pending outbox events", e)
            throw e
        }
    }
}