package com.restaurant.order.core.domain.model.vo

import java.time.LocalDateTime
import java.time.LocalTime

data class Availability(
        val isAvailable: Boolean,
        val stockQuantity: Int,
        val operatingHours: OperatingHours
) {
  init {
    require(stockQuantity >= 0) { "재고 수량은 0 이상이어야 합니다." }
  }

  fun isAvailableNow(): Boolean {
    if (!isAvailable || stockQuantity <= 0) return false
    return operatingHours.isWithinOperatingHours(LocalDateTime.now())
  }

  fun decreaseStock(quantity: Int): Availability {
    require(quantity > 0) { "감소시킬 수량은 0보다 커야 합니다." }
    require(stockQuantity >= quantity) { "재고가 부족합니다." }
    return copy(stockQuantity = stockQuantity - quantity)
  }

  companion object {
    fun empty() =
            Availability(
                    isAvailable = false,
                    stockQuantity = 0,
                    operatingHours =
                            OperatingHours(startTime = LocalTime.MIN, endTime = LocalTime.MAX)
            )
  }
}
