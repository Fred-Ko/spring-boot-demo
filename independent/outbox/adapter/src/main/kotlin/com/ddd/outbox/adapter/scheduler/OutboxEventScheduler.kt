package com.ddd.outbox.adapter.scheduler

import com.ddd.outbox.core.application.service.OutboxService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxEventScheduler(
    private val outboxService: OutboxService
) {
    @Scheduled(fixedDelayString = "\${outbox.scheduler.fixed-delay:5000}")
    fun publishPendingEvents() {
        outboxService.publishPendingEvents()
    }
}