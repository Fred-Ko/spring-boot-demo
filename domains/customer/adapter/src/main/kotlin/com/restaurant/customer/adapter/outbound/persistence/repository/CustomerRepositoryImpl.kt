package com.restaurant.customer.adapter.outbound.persistence.repository

import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.restaurant.customer.adapter.outbound.persistence.entity.CustomerEntity
import com.restaurant.customer.adapter.outbound.persistence.mapper.CustomerMapper
import com.restaurant.customer.core.domain.model.Customer
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import com.restaurant.customer.core.domain.repository.CustomerRepository
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CustomerRepositoryImpl(
    private val entityManager: EntityManager,
    private val queryFactory: SpringDataQueryFactory,
    private val customerMapper: CustomerMapper,
    private val applicationEventPublisher: ApplicationEventPublisher
) : CustomerRepository {

    override fun save(customer: Customer): Customer {
        val entity = customerMapper.toEntity(customer)
        val savedCustomer = customerMapper.toDomain(
            if (entity.id == UUID(0, 0)) {
                entityManager.persist(entity)
                entity
            } else {
                entityManager.merge(entity)
            }
        )

        // 도메인 이벤트 발행
        customer.getAndClearDomainEvents().forEach { event ->
            applicationEventPublisher.publishEvent(event)
            println("Published event: $event")
        }

        return savedCustomer
    }

    override fun findById(id: UUID): Customer? {
        return queryFactory.singleQuery {
            select(entity(CustomerEntity::class))
            from(entity(CustomerEntity::class))
            where(col(CustomerEntity::id).equal(id))
        }?.let(customerMapper::toDomain)
    }

    override fun findByEmail(email: Email): Customer? {
        return queryFactory.singleQuery {
            select(entity(CustomerEntity::class))
            from(entity(CustomerEntity::class))
            where(col(CustomerEntity::email).equal(email.value))
        }?.let(customerMapper::toDomain)
    }

    override fun findByPhoneNumber(phoneNumber: PhoneNumber): Customer? {
        return queryFactory.singleQuery {
            select(entity(CustomerEntity::class))
            from(entity(CustomerEntity::class))
            where(col(CustomerEntity::phoneNumber).equal(phoneNumber.value))
        }?.let(customerMapper::toDomain)
    }

    override fun delete(customer: Customer) {
        entityManager.remove(
            entityManager.getReference(CustomerEntity::class.java, customer.id)
        )
    }

    override fun existsByEmail(email: Email): Boolean {
        val count = queryFactory.singleQuery<Long> {
            select(count(col(CustomerEntity::id)))
            from(entity(CustomerEntity::class))
            where(col(CustomerEntity::email).equal(email.value))
        }
        return count > 0
    }

    override fun existsByPhoneNumber(phoneNumber: PhoneNumber): Boolean {
        val count = queryFactory.singleQuery<Long> {
            select(count(col(CustomerEntity::id)))
            from(entity(CustomerEntity::class))
            where(col(CustomerEntity::phoneNumber).equal(phoneNumber.value))
        }
        return count > 0
    }

    override fun findAll(pageable: Pageable): Page<Customer> {
        val query = queryFactory.listQuery<CustomerEntity> {
            select(entity(CustomerEntity::class))
            from(entity(CustomerEntity::class))
            offset(pageable.offset.toInt())
            limit(pageable.pageSize)
        }

        val countQuery = queryFactory.singleQuery<Long> {
            select(count(col(CustomerEntity::id)))
            from(entity(CustomerEntity::class))
        }

        val content = query.map(customerMapper::toDomain)
        val total = countQuery ?: 0

        return PageImpl(content, pageable, total)
    }
}