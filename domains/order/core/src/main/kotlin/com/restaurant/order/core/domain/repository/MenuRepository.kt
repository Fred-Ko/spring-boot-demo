package com.restaurant.order.core.domain.repository

import com.restaurant.order.core.domain.model.Menu
import com.restaurant.order.core.domain.model.vo.Category
import java.util.*

interface MenuRepository {
  fun save(menu: Menu): Menu
  fun findById(id: UUID): Menu?
  fun findByIdOrThrow(id: UUID): Menu
  fun findAll(): List<Menu>
  fun findByCategory(category: Category): List<Menu>
  fun findAvailable(): List<Menu>
  fun delete(menu: Menu)
  fun existsById(id: UUID): Boolean
}
