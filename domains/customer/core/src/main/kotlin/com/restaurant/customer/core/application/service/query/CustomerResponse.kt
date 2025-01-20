package com.restaurant.customer.core.application.service.query

import com.restaurant.customer.core.domain.model.Customer
import java.time.LocalDateTime
import java.util.*

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