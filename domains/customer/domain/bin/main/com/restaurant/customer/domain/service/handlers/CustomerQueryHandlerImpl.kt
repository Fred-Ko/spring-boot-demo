package com.restaurant.customer.domain.service.handlers

import com.example.customer.domain.Customer
import com.example.customer.domain.CustomerGrade
import com.example.customer.domain.CustomerInquiry
import com.restaurant.customer.presentation.queries.GetCustomerQuery
import com.restaurant.customer.presentation.queries.GetCustomerGradeQuery
import com.restaurant.customer.presentation.queries.GetCustomerInquiriesQuery


class CustomerQueryHandlerImpl : CustomerQueryHandler {
    override fun handle(query: GetCustomerQuery): Customer {
        // 고객 정보 조회 로직 구현
        return Customer() // 예시 반환값
    }

    override fun handle(query: GetCustomerGradeQuery): CustomerGrade {
        // 고객 등급 조회 로직 구현
        return CustomerGrade() // 예시 반환값
    }

    override fun handle(query: GetCustomerInquiriesQuery): List<CustomerInquiry> {
        // 고객 문의 조회 로직 구현
        return listOf(CustomerInquiry()) // 예시 반환값
    }
} 