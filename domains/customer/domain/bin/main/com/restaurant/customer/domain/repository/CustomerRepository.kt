package com.restaurant.customer.domain.repository

import com.example.customer.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long> 