package com.restaurant.order.adapter.outbound.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "menus")
class MenuEntity(
        @Id @Column(columnDefinition = "BINARY(16)") var id: UUID = UUID(0, 0),
        @Column(nullable = false) var name: String,
        @Column(nullable = false, length = 500) var description: String,
        @Column(nullable = false) var price: BigDecimal,
        @Embedded var category: CategoryEmbeddable,
        @Embedded var availability: AvailabilityEmbeddable,
        @Column(nullable = false) var createdAt: LocalDateTime,
        @Column(nullable = false) var updatedAt: LocalDateTime,
        @Version var version: Long = 0
)

@Embeddable
class CategoryEmbeddable(
        @Column(nullable = false) var name: String,
        @Column var description: String?
)

@Embeddable
class AvailabilityEmbeddable(
        @Column(nullable = false) var stockQuantity: Int,
        @Column(nullable = false) var operatingStartTime: LocalTime,
        @Column(nullable = false) var operatingEndTime: LocalTime,
        @Column(nullable = false) var isAvailable: Boolean
)
