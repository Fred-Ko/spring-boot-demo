package com.ddd.outbox.adapter.outbound.persistence.repository

import com.ddd.outbox.adapter.outbound.persistence.entity.OutboxEventEntity
import com.ddd.outbox.adapter.outbound.persistence.mapper.OutboxEventMapper
import com.ddd.outbox.core.domain.event.OutboxEvent
import com.ddd.outbox.core.domain.repository.OutboxEventRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SpringDataJpaOutboxEventRepository : JpaRepository<OutboxEventEntity, UUID> {
    @Query("SELECT o FROM OutboxEventEntity o WHERE o.publishedAt IS NULL ORDER BY o.createdAt ASC")
    fun findUnpublishedEvents(): List<OutboxEventEntity>
}

@Repository
class JpaOutboxEventRepository(
    private val repository: SpringDataJpaOutboxEventRepository
) : OutboxEventRepository {
    override fun save(event: OutboxEvent): OutboxEvent {
        val entity = OutboxEventMapper.toEntity(event)
        return OutboxEventMapper.toDomain(repository.save(entity))
    }

    override fun findUnpublishedEvents(): List<OutboxEvent> {
        return repository.findUnpublishedEvents().map(OutboxEventMapper::toDomain)
    }
}