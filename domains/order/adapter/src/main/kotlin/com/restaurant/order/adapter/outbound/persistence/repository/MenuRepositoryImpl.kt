package com.restaurant.order.adapter.outbound.persistence.repository

import com.restaurant.order.adapter.outbound.persistence.mapper.MenuMapper
import com.restaurant.order.core.domain.model.Menu
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.repository.MenuRepository
import java.util.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class MenuRepositoryImpl(
        private val menuJpaRepository: MenuJpaRepository,
        private val menuMapper: MenuMapper,
        private val applicationEventPublisher: ApplicationEventPublisher
) : MenuRepository {

  override fun save(menu: Menu): Menu {
    val entity = menuMapper.toEntity(menu)
    val savedEntity = menuJpaRepository.save(entity)
    val savedMenu = menuMapper.toDomain(savedEntity)

    menu.getAndClearDomainEvents().forEach { event ->
      applicationEventPublisher.publishEvent(event)
    }

    return savedMenu
  }

  override fun findById(id: UUID): Menu? {
    return menuJpaRepository.findById(id).map(menuMapper::toDomain).orElse(null)
  }

  override fun findByIdOrThrow(id: UUID): Menu {
    return findById(id) ?: throw MenuNotFoundException(id)
  }

  override fun findAll(pageable: Pageable): Page<Menu> {
    return menuJpaRepository.findAll(pageable).map(menuMapper::toDomain)
  }

  override fun findByCategory(category: Category): List<Menu> {
    return menuJpaRepository.findByCategory(category).map(menuMapper::toDomain)
  }

  override fun findAvailable(pageable: Pageable): Page<Menu> {
    return menuJpaRepository.findByAvailabilityIsAvailable(true, pageable).map(menuMapper::toDomain)
  }

  override fun delete(menu: Menu) {
    menuJpaRepository.deleteById(menu.id)
  }

  override fun existsById(id: UUID): Boolean {
    return menuJpaRepository.existsById(id)
  }
}

class MenuNotFoundException(id: UUID) : RuntimeException("Menu not found with id: $id")
