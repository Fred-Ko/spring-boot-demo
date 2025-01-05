package com.restaurant.customer.persistence.repository

import com.restaurant.customer.persistence.entities.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long> 