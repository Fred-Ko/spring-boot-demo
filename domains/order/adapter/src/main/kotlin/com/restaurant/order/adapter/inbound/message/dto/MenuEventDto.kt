package com.restaurant.order.adapter.inbound.message.dto

import com.restaurant.order.core.domain.event.*
import com.restaurant.order.core.domain.model.Menu
import com.restaurant.order.core.domain.model.vo.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class EventEnvelope(
        val id: UUID,
        val aggregateType: String,
        val aggregateId: UUID,
        val eventType: String,
        val version: Long,
        val timestamp: List<Int>,
        val payload: Map<String, Any>
)

sealed interface MenuEventDto {
  fun toDomainEvent(): MenuEvent
}

data class MenuCreatedEventDto(
        val menuId: UUID,
        val name: String,
        val description: String,
        val price: BigDecimal,
        val category: CategoryDto,
        val availability: AvailabilityDto,
        val eventType: String
) : MenuEventDto {
  override fun toDomainEvent() =
          MenuCreatedEventV1(
                  Menu.from(
                          id = menuId,
                          name = name,
                          description = description,
                          price = Price(price),
                          category = category.toDomain(),
                          availability = availability.toDomain(),
                          createdAt = LocalDateTime.now(),
                          updatedAt = LocalDateTime.now(),
                          version = 0
                  )
          )
}

data class MenuPriceUpdatedEventDto(
        val menuId: UUID,
        val price: BigDecimal,
        val eventType: String
) : MenuEventDto {
  override fun toDomainEvent() =
          MenuPriceUpdatedEventV1(Menu.forPriceUpdate(id = menuId, price = Price(price)))
}

data class MenuAvailabilityUpdatedEventDto(
        val menuId: UUID,
        val availability: AvailabilityDto,
        val eventType: String
) : MenuEventDto {
  override fun toDomainEvent() =
          MenuAvailabilityUpdatedEventV1(
                  Menu.forAvailabilityUpdate(id = menuId, availability = availability.toDomain())
          )
}

data class MenuStockDecreasedEventDto(val menuId: UUID, val quantity: Int, val eventType: String) :
        MenuEventDto {
  override fun toDomainEvent() =
          MenuStockDecreasedEventV1(Menu.forStockUpdate(id = menuId, quantity = quantity), quantity)
}

data class CategoryDto(val name: String, val description: String?) {
  fun toDomain() = Category(name = name, description = description)
}

data class AvailabilityDto(
        val stockQuantity: Int,
        val operatingStartTime: LocalTime,
        val operatingEndTime: LocalTime,
        val isAvailable: Boolean
) {
  fun toDomain() =
          Availability(
                  isAvailable = isAvailable,
                  stockQuantity = stockQuantity,
                  operatingHours =
                          OperatingHours(startTime = operatingStartTime, endTime = operatingEndTime)
          )
}
