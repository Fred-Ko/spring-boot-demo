package com.restaurant.customer.domain.service.handlers

import com.restaurant.customer.domain.mapper.CustomerMapper
import com.restaurant.customer.domain.model.CustomerInquiryModel
import com.restaurant.customer.domain.model.CustomerModel
import com.restaurant.customer.domain.repository.entities.Customer
import com.restaurant.customer.domain.repository.entities.CustomerGrade
import com.restaurant.customer.domain.repository.entities.CustomerInquiry
import com.restaurant.customer.presentation.queries.GetCustomerQuery
import com.restaurant.customer.presentation.queries.GetCustomerGradeQuery
import com.restaurant.customer.presentation.queries.GetCustomerInquiriesQuery


class CustomerQueryHandlerImpl : CustomerQueryHandler {
    override fun handle(query: GetCustomerQuery): CustomerModel {
        // 고객 정보 조회 로직 구현
        return CustomerMapper.toModel(Customer()) // 예시 반환값
    }

    override fun handle(query: GetCustomerGradeQuery): CustomerGrade {
        // 고객 등급 조회 로직 구현
        return CustomerGrade.BRONZE // 예시 반환값
    }

    override fun handle(query: GetCustomerInquiriesQuery): List<CustomerInquiryModel> {
        // 고객 문의 조회 로직 구현
        return listOf(CustomerMapper.toModel(CustomerInquiry())) // 예시 반환값
    }
} 