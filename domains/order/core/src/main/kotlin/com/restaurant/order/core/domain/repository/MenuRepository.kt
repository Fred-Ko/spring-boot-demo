package com.restaurant.order.core.domain.repository

import com.restaurant.order.core.domain.model.Menu
import com.restaurant.order.core.domain.model.vo.Category
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MenuRepository {
  fun save(menu: Menu): Menu
  fun findById(id: UUID): Menu?
  fun findByIdOrThrow(id: UUID): Menu
  fun findAll(pageable: Pageable): Page<Menu>
  fun findByCategory(category: Category): List<Menu>
  fun findAvailable(pageable: Pageable): Page<Menu>
  fun delete(menu: Menu)
  fun existsById(id: UUID): Boolean
}
