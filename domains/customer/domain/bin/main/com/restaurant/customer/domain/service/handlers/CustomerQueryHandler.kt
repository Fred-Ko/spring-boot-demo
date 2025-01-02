package com.restaurant.customer.domain.service.handlers

import com.restaurant.customer.presentation.queries.GetCustomerQuery
import com.restaurant.customer.presentation.queries.GetCustomerGradeQuery
import com.restaurant.customer.presentation.queries.GetCustomerInquiriesQuery
import com.restaurant.customer.presentation.dto.CustomerDto
import com.restaurant.customer.presentation.dto.CustomerGradeDto
import com.restaurant.customer.presentation.dto.CustomerInquiryDto

interface CustomerQueryHandler {
    fun handle(query: GetCustomerQuery): CustomerDto
    fun handle(query: GetCustomerGradeQuery): CustomerGradeDto
    fun handle(query: GetCustomerInquiriesQuery): List<CustomerInquiryDto>
}
