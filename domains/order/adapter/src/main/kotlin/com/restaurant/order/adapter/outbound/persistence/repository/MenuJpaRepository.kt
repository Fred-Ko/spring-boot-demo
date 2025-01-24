package com.restaurant.order.adapter.outbound.persistence.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.restaurant.order.adapter.outbound.persistence.entity.MenuEntity
import com.restaurant.order.core.domain.model.vo.Category
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuJpaRepository : JpaRepository<MenuEntity, UUID>, KotlinJdslJpqlExecutor {
  fun findByCategory(category: Category): List<MenuEntity>
  fun findByAvailabilityIsAvailable(isAvailable: Boolean, pageable: Pageable): Page<MenuEntity>
}
