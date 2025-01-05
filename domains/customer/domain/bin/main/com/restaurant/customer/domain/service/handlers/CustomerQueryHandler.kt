package com.restaurant.customer.domain.service.handlers

import com.restaurant.customer.domain.model.CustomerInquiryModel
import com.restaurant.customer.domain.model.CustomerModel
import com.restaurant.customer.domain.repository.entities.CustomerGrade
import com.restaurant.customer.presentation.queries.GetCustomerQuery
import com.restaurant.customer.presentation.queries.GetCustomerGradeQuery
import com.restaurant.customer.presentation.queries.GetCustomerInquiriesQuery

interface CustomerQueryHandler {
    fun handle(query: GetCustomerQuery): CustomerModel
    fun handle(query: GetCustomerGradeQuery): CustomerGrade
    fun handle(query: GetCustomerInquiriesQuery): List<CustomerInquiryModel>
}
