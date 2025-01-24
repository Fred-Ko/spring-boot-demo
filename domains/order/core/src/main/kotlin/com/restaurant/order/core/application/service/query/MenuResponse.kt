package com.restaurant.order.core.application.service.query

import com.restaurant.order.core.domain.model.Menu
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

data class MenuResponse(
        val id: UUID,
        val name: String,
        val description: String,
        val price: BigDecimal,
        val category: CategoryResponse,
        val availability: AvailabilityResponse,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
) {
        data class CategoryResponse(val name: String, val description: String?)

        data class AvailabilityResponse(
                val isAvailable: Boolean,
                val stockQuantity: Int,
                val operatingStartTime: LocalTime,
                val operatingEndTime: LocalTime
        )

        companion object {
                fun from(menu: Menu): MenuResponse {
                        return MenuResponse(
                                id = menu.id,
                                name = menu.name,
                                description = menu.description,
                                price = menu.getPrice().amount,
                                category =
                                        CategoryResponse(
                                                name = menu.category.name,
                                                description = menu.category.description
                                        ),
                                availability =
                                        AvailabilityResponse(
                                                isAvailable = menu.isAvailable(),
                                                stockQuantity = menu.availability.stockQuantity,
                                                operatingStartTime =
                                                        menu.availability.operatingHours.startTime,
                                                operatingEndTime =
                                                        menu.availability.operatingHours.endTime
                                        ),
                                createdAt = menu.createdAt,
                                updatedAt = menu.updatedAt
                        )
                }
        }
}
