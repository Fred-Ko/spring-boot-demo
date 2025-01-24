package com.restaurant.customer.adapter.outbound.persistence.repository

import com.restaurant.customer.adapter.outbound.persistence.mapper.CustomerMapper
import com.restaurant.customer.core.domain.model.Customer
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import com.restaurant.customer.core.domain.repository.CustomerRepository
import java.util.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CustomerRepositoryImpl(
        private val customerJpaRepository: CustomerJpaRepository,
        private val customerMapper: CustomerMapper,
        private val applicationEventPublisher: ApplicationEventPublisher
) : CustomerRepository {

    override fun save(customer: Customer): Customer {
        val entity = customerMapper.toEntity(customer)
        val savedEntity = customerJpaRepository.save(entity)
        val savedCustomer = customerMapper.toDomain(savedEntity)

        // 도메인 이벤트 발행
        customer.getAndClearDomainEvents().forEach { event ->
            applicationEventPublisher.publishEvent(event)
        }

        return savedCustomer
    }

    override fun findById(id: UUID): Customer? {
        return customerJpaRepository.findById(id).map(customerMapper::toDomain).orElse(null)
    }

    override fun findByEmail(email: Email): Customer? {
        return customerJpaRepository.findByEmail(email.value)?.let { customerMapper.toDomain(it) }
    }

    override fun findByPhoneNumber(phoneNumber: PhoneNumber): Customer? {
        return customerJpaRepository.findByPhoneNumber(phoneNumber.value)?.let {
            customerMapper.toDomain(it)
        }
    }

    override fun delete(customer: Customer) {
        customerJpaRepository.deleteById(customer.id)
    }

    override fun existsByEmail(email: Email): Boolean {
        return customerJpaRepository.existsByEmail(email.value)
    }

    override fun existsByPhoneNumber(phoneNumber: PhoneNumber): Boolean {
        return customerJpaRepository.existsByPhoneNumber(phoneNumber.value)
    }

    override fun findAll(pageable: Pageable): Page<Customer> {
        return customerJpaRepository.findAll(pageable).map(customerMapper::toDomain)
    }
}
