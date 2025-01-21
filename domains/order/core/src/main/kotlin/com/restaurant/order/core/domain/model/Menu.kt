package com.restaurant.order.core.domain.model

import com.restaurant.order.core.domain.event.MenuAvailabilityUpdatedEventV1
import com.restaurant.order.core.domain.event.MenuCreatedEventV1
import com.restaurant.order.core.domain.event.MenuPriceUpdatedEventV1
import com.restaurant.order.core.domain.event.MenuStockDecreasedEventV1
import com.restaurant.order.core.domain.model.vo.Availability
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.model.vo.Price
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import org.springframework.data.domain.AbstractAggregateRoot

// 운영 시간 객체
data class OperatingHours(val startTime: LocalTime, val endTime: LocalTime) {
  init {
    require(startTime.isBefore(endTime)) { "시작 시간은 종료 시간보다 이전이어야 합니다." }
  }

  fun isWithinOperatingHours(dateTime: LocalDateTime): Boolean {
    val time = dateTime.toLocalTime()
    return !time.isBefore(startTime) && !time.isAfter(endTime)
  }
}

class Menu
private constructor(
        val id: UUID,
        val name: String,
        val description: String,
        private var price: Price,
        private var availability: Availability,
        val category: Category,
        val createdAt: LocalDateTime,
        var updatedAt: LocalDateTime,
        var version: Long = 0
) : AbstractAggregateRoot<Menu>() {

  init {
    require(id != UUID(0, 0)) { "메뉴 ID는 필수입니다." }
    require(name.isNotBlank()) { "메뉴 이름은 비어있을 수 없습니다." }
    require(description.isNotBlank()) { "메뉴 설명은 비어있을 수 없습니다." }
  }

  fun getAndClearDomainEvents(): MutableCollection<Any> {
    val events = domainEvents().toMutableList()
    clearDomainEvents()
    return events
  }

  companion object {
    fun create(
            name: String,
            description: String,
            price: Price,
            availability: Availability,
            category: Category
    ): Menu {
      val now = LocalDateTime.now()
      return Menu(
                      id = UUID.randomUUID(),
                      name = name,
                      description = description,
                      price = price,
                      availability = availability,
                      category = category,
                      createdAt = now,
                      updatedAt = now,
                      version = 0
              )
              .also { it.registerEvent(MenuCreatedEventV1(it)) }
    }

    fun from(
            id: UUID,
            name: String,
            description: String,
            price: Price,
            availability: Availability,
            category: Category,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            version: Long
    ): Menu {
      return Menu(
              id = id,
              name = name,
              description = description,
              price = price,
              availability = availability,
              category = category,
              createdAt = createdAt,
              updatedAt = updatedAt,
              version = version
      )
    }
  }

  // 메뉴 가용성 확인
  fun isAvailable(): Boolean = availability.isAvailableNow()

  // 재고 감소
  fun decreaseStock(quantity: Int) {
    availability = availability.decreaseStock(quantity)
    updatedAt = LocalDateTime.now()
    registerEvent(MenuStockDecreasedEventV1(this, quantity))
  }

  // 가격 조회
  fun getPrice(): Price = price

  // 할인 적용
  fun updatePrice(newPrice: Price) {
    this.price = newPrice
    updatedAt = LocalDateTime.now()
    registerEvent(MenuPriceUpdatedEventV1(this))
  }

  // 가용성 업데이트
  fun updateAvailability(newAvailability: Availability) {
    this.availability = newAvailability
    updatedAt = LocalDateTime.now()
    registerEvent(MenuAvailabilityUpdatedEventV1(this))
  }

  // 운영 시간 내 여부 확인
  fun isWithinOperatingHours(dateTime: LocalDateTime): Boolean =
          availability.operatingHours.isWithinOperatingHours(dateTime)

  // 재고 확인
  fun hasEnoughStock(quantity: Int): Boolean = availability.stockQuantity >= quantity

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Menu
    return id == other.id
  }

  override fun hashCode(): Int = id.hashCode()
}
