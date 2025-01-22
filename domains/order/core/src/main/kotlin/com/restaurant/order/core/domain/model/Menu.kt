package com.restaurant.order.core.domain.model

import com.restaurant.order.core.domain.event.MenuAvailabilityUpdatedEventV1
import com.restaurant.order.core.domain.event.MenuCreatedEventV1
import com.restaurant.order.core.domain.event.MenuPriceUpdatedEventV1
import com.restaurant.order.core.domain.event.MenuStockDecreasedEventV1
import com.restaurant.order.core.domain.model.vo.Availability
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.model.vo.Price
import java.time.LocalDateTime
import java.util.*
import org.springframework.data.domain.AbstractAggregateRoot

class Menu
private constructor(
        val id: UUID,
        private var _name: String,
        private var _description: String,
        private var price: Price,
        private var _availability: Availability,
        private var _category: Category,
        val createdAt: LocalDateTime,
        var updatedAt: LocalDateTime,
        var version: Long = 0
) : AbstractAggregateRoot<Menu>() {

  val name: String
    get() = _name
  val description: String
    get() = _description
  val category: Category
    get() = _category
  val availability: Availability
    get() = _availability

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
      val menu =
              Menu(
                      id = UUID.randomUUID(),
                      _name = name,
                      _description = description,
                      price = price,
                      _availability = availability,
                      _category = category,
                      createdAt = now,
                      updatedAt = now
              )
      menu.registerEvent(MenuCreatedEventV1(menu))
      return menu
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
              _name = name,
              _description = description,
              price = price,
              _availability = availability,
              _category = category,
              createdAt = createdAt,
              updatedAt = updatedAt,
              version = version
      )
    }

    fun forPriceUpdate(id: UUID, price: Price): Menu {
      val now = LocalDateTime.now()
      return Menu(
              id = id,
              _name = "",
              _description = "",
              price = price,
              _availability = Availability.empty(),
              _category = Category.empty(),
              createdAt = now,
              updatedAt = now
      )
    }

    fun forAvailabilityUpdate(id: UUID, availability: Availability): Menu {
      val now = LocalDateTime.now()
      return Menu(
              id = id,
              _name = "",
              _description = "",
              price = Price.ZERO,
              _availability = availability,
              _category = Category.empty(),
              createdAt = now,
              updatedAt = now
      )
    }

    fun forStockUpdate(id: UUID, quantity: Int): Menu {
      val now = LocalDateTime.now()
      return Menu(
              id = id,
              _name = "",
              _description = "",
              price = Price.ZERO,
              _availability = Availability.empty().copy(stockQuantity = quantity),
              _category = Category.empty(),
              createdAt = now,
              updatedAt = now
      )
    }
  }

  // 메뉴 가용성 확인
  fun isAvailable(): Boolean = availability.isAvailableNow()

  // 재고 감소
  fun decreaseStock(quantity: Int) {
    _availability = _availability.decreaseStock(quantity)
    updatedAt = LocalDateTime.now()
    registerEvent(MenuStockDecreasedEventV1(this, quantity))
  }

  // 가격 조회
  fun getPrice(): Price = price

  // 가격 업데이트
  fun updatePrice(newPrice: Price) {
    this.price = newPrice
    updatedAt = LocalDateTime.now()
    registerEvent(MenuPriceUpdatedEventV1(this))
  }

  // 이름 업데이트
  fun updateName(newName: String) {
    require(newName.isNotBlank()) { "메뉴 이름은 비어있을 수 없습니다." }
    this._name = newName
    updatedAt = LocalDateTime.now()
  }

  // 설명 업데이트
  fun updateDescription(newDescription: String) {
    require(newDescription.isNotBlank()) { "메뉴 설명은 비어있을 수 없습니다." }
    this._description = newDescription
    updatedAt = LocalDateTime.now()
  }

  // 카테고리 업데이트
  fun updateCategory(newCategory: Category) {
    this._category = newCategory
    updatedAt = LocalDateTime.now()
  }

  // 가용성 업데이트
  fun updateAvailability(newAvailability: Availability) {
    this._availability = newAvailability
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
