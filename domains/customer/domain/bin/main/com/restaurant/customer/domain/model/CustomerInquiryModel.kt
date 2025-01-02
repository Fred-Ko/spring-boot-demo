package com.restaurant.customer.domain.model

data class CustomerInquiryModel(
    val id: Long = 0,
    val inquiryDetails: String,
    val customer: CustomerModel
) 