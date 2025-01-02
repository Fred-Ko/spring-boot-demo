package com.restaurant.customer.domain.repository

import com.example.customer.domain.CustomerInquiry
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerInquiryRepository : JpaRepository<CustomerInquiry, Long> 