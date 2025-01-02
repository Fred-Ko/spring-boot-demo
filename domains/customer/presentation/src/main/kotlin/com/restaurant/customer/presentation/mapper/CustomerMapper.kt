package com.restaurant.customer.presentation.mapper

import com.restaurant.customer.domain.model.CustomerModel
import com.restaurant.customer.domain.model.CustomerInquiryModel
import com.restaurant.customer.domain.repository.entities.CustomerGrade
import com.restaurant.customer.presentation.dto.CustomerDto
import com.restaurant.customer.presentation.dto.CustomerGradeDto
import com.restaurant.customer.presentation.dto.CustomerInquiryDto

object CustomerMapper {

    fun toDto(customerModel: CustomerModel): CustomerDto {
        return CustomerDto(
            name = customerModel.name,
            email = customerModel.email
        )
    }

    fun toDto(customerGrade: CustomerGrade): CustomerGradeDto {
        return CustomerGradeDto(
            grade = customerGrade.name
        )
    }

    fun toDto(customerInquiryModel: CustomerInquiryModel): CustomerInquiryDto {
        return CustomerInquiryDto(
            message = customerInquiryModel.inquiryDetails
        )
    }
} 