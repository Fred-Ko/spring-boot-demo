package com.restaurant.order.core.domain.model.vo

data class Category(val name: String, val description: String?) {
  companion object {
    fun empty() = Category(name = "", description = null)
  }
}
