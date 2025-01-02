package com.restaurant.customer.domain.mapper

import com.restaurant.customer.domain.repository.entities.Customer
import com.restaurant.customer.domain.repository.entities.CustomerInfo
import com.restaurant.customer.domain.repository.entities.CustomerInquiry
import com.restaurant.customer.domain.model.CustomerModel
import com.restaurant.customer.domain.model.CustomerInfoModel
import com.restaurant.customer.domain.model.CustomerInquiryModel

object CustomerMapper {
    fun toModel(customer: Customer): CustomerModel {
        return CustomerModel(
            id = customer.id,
            name = customer.name,
            email = customer.email,
            customerInfo = toModel(customer.customerInfo),
            customerGrade = customer.customerGrade
        )
    }

    fun toEntity(customerModel: CustomerModel): Customer {
        return Customer(
            id = customerModel.id,
            name = customerModel.name,
            email = customerModel.email,
            customerInfo = toEntity(customerModel.customerInfo),
            customerGrade = customerModel.customerGrade
        )
    }

    private fun toModel(customerInfo: CustomerInfo): CustomerInfoModel {
        return CustomerInfoModel(
            id = customerInfo.id,
            address = customerInfo.address,
            phoneNumber = customerInfo.phoneNumber
        )
    }

    private fun toEntity(customerInfoModel: CustomerInfoModel): CustomerInfo {
        return CustomerInfo(
            id = customerInfoModel.id,
            address = customerInfoModel.address,
            phoneNumber = customerInfoModel.phoneNumber
        )
    }

    fun toModel(inquiry: CustomerInquiry): CustomerInquiryModel {
        return CustomerInquiryModel(
            id = inquiry.id,
            inquiryDetails = inquiry.inquiryDetails,
            customer = toModel(inquiry.customer)
        )
    }

    fun toEntity(inquiryModel: CustomerInquiryModel): CustomerInquiry {
        return CustomerInquiry(
            id = inquiryModel.id,
            inquiryDetails = inquiryModel.inquiryDetails,
            customer = toEntity(inquiryModel.customer)
        )
    }
} 