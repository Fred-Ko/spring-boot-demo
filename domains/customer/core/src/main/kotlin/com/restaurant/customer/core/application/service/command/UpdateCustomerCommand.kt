package com.restaurant.customer.core.application.service.command

import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber

data class UpdateCustomerCommand(
    val name: NameUpdate? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val address: AddressUpdate? = null
) {
    fun toCustomerName() = name?.let { CustomerName(it.firstName, it.lastName) }
    fun toEmail() = email?.let { Email(it) }
    fun toPhoneNumber() = phoneNumber?.let { PhoneNumber(it) }
    fun toAddress() = address?.let { Address(it.zipCode, it.street, it.city) }

    data class NameUpdate(
        val firstName: String,
        val lastName: String
    )

    data class AddressUpdate(
        val zipCode: String,
        val street: String,
        val city: String
    )
} 