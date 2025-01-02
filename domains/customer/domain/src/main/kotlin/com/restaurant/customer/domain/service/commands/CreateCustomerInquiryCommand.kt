package com.restaurant.customer.domain.service.commands

data class CreateCustomerInquiryCommand(
    val id: Long,
    val message: String
) 