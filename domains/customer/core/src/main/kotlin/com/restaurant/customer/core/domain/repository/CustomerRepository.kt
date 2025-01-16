package com.restaurant.customer.core.domain.repository

import com.restaurant.customer.core.domain.model.Customer
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface CustomerRepository {
    fun save(customer: Customer): Customer
    fun findById(id: UUID): Customer?
    fun findByEmail(email: Email): Customer?
    fun findByPhoneNumber(phoneNumber: PhoneNumber): Customer?
    fun delete(customer: Customer)
    fun existsByEmail(email: Email): Boolean
    fun existsByPhoneNumber(phoneNumber: PhoneNumber): Boolean
    fun findAll(pageable: Pageable): Page<Customer>
}