package com.restaurant.order.adapter.outbound.persistence.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.path
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import com.restaurant.order.adapter.outbound.persistence.entity.MenuEntity
import com.restaurant.order.adapter.outbound.persistence.mapper.MenuMapper
import com.restaurant.order.core.domain.model.Menu
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.repository.MenuRepository
import jakarta.persistence.EntityManager
import java.util.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class MenuRepositoryImpl(
        private val entityManager: EntityManager,
        private val queryFactory: SpringDataQueryFactory,
        private val menuMapper: MenuMapper,
        private val applicationEventPublisher: ApplicationEventPublisher
) : MenuRepository {

  override fun save(menu: Menu): Menu {
    val entity = menuMapper.toEntity(menu)
    val savedEntity =
            if (entity.id == UUID(0, 0)) {
              entityManager.persist(entity)
              entity
            } else {
              entityManager.merge(entity)
            }
    val savedMenu = menuMapper.toDomain(savedEntity)

    // 도메인 이벤트 발행
    menu.getAndClearDomainEvents().forEach { event ->
      applicationEventPublisher.publishEvent(event)
      println("Published event: $event")
    }

    return savedMenu
  }

  override fun findById(id: UUID): Menu? {
    val entity =
            queryFactory.singleQuery<MenuEntity?> {
              select(entity(MenuEntity::class))
              from(entity(MenuEntity::class))
              where(col(MenuEntity::id).equal(id))
            }
    return entity?.let { menuMapper.toDomain(it) }
  }

  override fun findByIdOrThrow(id: UUID): Menu {
    return findById(id) ?: throw NoSuchElementException("Menu not found with id: $id")
  }

  override fun findAll(): List<Menu> {
    val entities =
            queryFactory.listQuery<MenuEntity> {
              select(entity(MenuEntity::class))
              from(entity(MenuEntity::class))
            }
    return entities.map { menuMapper.toDomain(it) }
  }

  override fun findByCategory(category: Category): List<Menu> {
    val entities =
            queryFactory.listQuery<MenuEntity> {
              select(entity(MenuEntity::class))
              from(entity(MenuEntity::class))
              where(path(MenuEntity::category).get("name").equal(category.name))
            }
    return entities.map { menuMapper.toDomain(it) }
  }

  override fun findAvailable(): List<Menu> {
    val entities =
            queryFactory.listQuery<MenuEntity> {
              select(entity(MenuEntity::class))
              from(entity(MenuEntity::class))
              where(path(MenuEntity::availability).get("isAvailable").equal(true))
            }
    return entities.map { menuMapper.toDomain(it) }
  }

  override fun delete(menu: Menu) {
    entityManager.remove(entityManager.getReference(MenuEntity::class.java, menu.id))
  }

  override fun existsById(id: UUID): Boolean {
    val count =
            queryFactory.singleQuery<Long> {
              select(count(col(MenuEntity::id)))
              from(entity(MenuEntity::class))
              where(col(MenuEntity::id).equal(id))
            }
    return count > 0
  }
}
