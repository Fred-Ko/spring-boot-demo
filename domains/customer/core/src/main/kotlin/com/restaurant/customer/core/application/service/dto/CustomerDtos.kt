package com.restaurant.customer.core.application.service.dto

import com.restaurant.customer.core.domain.model.Customer
import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import java.time.LocalDateTime
import java.util.*

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

data class CustomerResponse(
    val id: UUID,
    val name: NameInfo,
    val email: String,
    val phoneNumber: String,
    val address: AddressInfo,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val version: Long
) {
    data class NameInfo(
        val firstName: String,
        val lastName: String
    )

    data class AddressInfo(
        val zipCode: String,
        val street: String,
        val city: String
    )

    companion object {
        fun from(customer: Customer) = CustomerResponse(
            id = customer.id,
            name = NameInfo(
                firstName = customer.name.firstName,
                lastName = customer.name.lastName
            ),
            email = customer.email.value,
            phoneNumber = customer.phoneNumber.value,
            address = AddressInfo(
                zipCode = customer.address.zipCode,
                street = customer.address.street,
                city = customer.address.city
            ),
            createdAt = customer.createdAt,
            updatedAt = customer.updatedAt,
            version = customer.version
        )
    }
}