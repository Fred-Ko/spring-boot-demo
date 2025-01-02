package com.restaurant.customer.domain.repository

import com.restaurant.customer.domain.repository.entities.CustomerInquiry
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerInquiryRepository : JpaRepository<CustomerInquiry, Long> 