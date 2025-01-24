package com.restaurant.customer.adapter.outbound.persistence.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.restaurant.customer.adapter.outbound.persistence.entity.CustomerEntity
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerJpaRepository : JpaRepository<CustomerEntity, UUID>, KotlinJdslJpqlExecutor {
  fun findByEmail(email: String): CustomerEntity?
  fun findByPhoneNumber(phoneNumber: String): CustomerEntity?
  fun existsByEmail(email: String): Boolean
  fun existsByPhoneNumber(phoneNumber: String): Boolean
}
