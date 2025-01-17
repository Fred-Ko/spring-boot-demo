package com.restaurant.customer.core.application.service.command

import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber

data class CreateCustomerCommand(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val zipCode: String,
    val street: String,
    val city: String
) {
    fun toCustomerName() = CustomerName(firstName, lastName)
    fun toEmail() = Email(email)
    fun toPhoneNumber() = PhoneNumber(phoneNumber)
    fun toAddress() = Address(zipCode, street, city)
} 