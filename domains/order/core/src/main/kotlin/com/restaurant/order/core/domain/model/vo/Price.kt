package com.restaurant.order.core.domain.model.vo

import java.math.BigDecimal

data class Price(val amount: BigDecimal, val currency: String = "KRW") {
  init {
    require(amount >= BigDecimal.ZERO) { "가격은 0 이상이어야 합니다." }
  }

  fun applyDiscount(discountRate: Double): Price {
    require(discountRate in 0.0..100.0) { "할인율은 0에서 100 사이여야 합니다." }
    val discountedAmount = amount.multiply(BigDecimal.valueOf(1 - discountRate / 100))
    return copy(amount = discountedAmount)
  }

  companion object {
    val ZERO = Price(BigDecimal.ZERO)
  }
}
