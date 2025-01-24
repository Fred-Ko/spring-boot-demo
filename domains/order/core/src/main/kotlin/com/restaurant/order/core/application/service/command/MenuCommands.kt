package com.restaurant.order.core.application.service.command

import com.restaurant.order.core.domain.model.vo.Availability
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.model.vo.OperatingHours
import java.math.BigDecimal
import java.time.LocalTime

data class CreateMenuCommand(
        val name: String,
        val description: String,
        val price: BigDecimal,
        val category: CategoryDto,
        val availability: AvailabilityDto
) {
        data class CategoryDto(val name: String, val description: String?) {
                fun toDomain(): Category {
                        return Category(name = name, description = description)
                }
        }

        data class AvailabilityDto(
                val stockQuantity: Int,
                val operatingStartTime: LocalTime,
                val operatingEndTime: LocalTime,
                val isAvailable: Boolean = true
        ) {
                fun toDomain() =
                        Availability(
                                isAvailable = isAvailable,
                                stockQuantity = stockQuantity,
                                operatingHours =
                                        OperatingHours(
                                                startTime = operatingStartTime,
                                                endTime = operatingEndTime
                                        )
                        )
        }
}

data class UpdateMenuCommand(
        val name: String? = null,
        val description: String? = null,
        val price: BigDecimal? = null,
        val category: CategoryInfo? = null,
        val availability: AvailabilityInfo? = null
) {
        data class CategoryInfo(val name: String, val description: String?)

        data class AvailabilityInfo(
                val stockQuantity: Int,
                val operatingStartTime: LocalTime,
                val operatingEndTime: LocalTime,
                val isAvailable: Boolean = true
        ) {
                fun toDomain() =
                        Availability(
                                isAvailable = isAvailable,
                                stockQuantity = stockQuantity,
                                operatingHours =
                                        OperatingHours(
                                                startTime = operatingStartTime,
                                                endTime = operatingEndTime
                                        )
                        )
        }
}
