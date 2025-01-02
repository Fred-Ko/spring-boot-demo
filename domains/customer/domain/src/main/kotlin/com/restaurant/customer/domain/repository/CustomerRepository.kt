package com.restaurant.customer.domain.repository

import com.restaurant.customer.domain.repository.entities.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long> 