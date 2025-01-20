package com.restaurant.customer.adapter.outbound.persistence.mapper

import com.restaurant.customer.adapter.outbound.persistence.entity.CustomerEntity
import com.restaurant.customer.core.domain.model.Customer
import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import org.springframework.stereotype.Component

@Component
class CustomerMapper {
    fun toDomain(entity: CustomerEntity): Customer {
        return Customer.from(
            id = entity.id,
            name = toCustomerName(entity),
            email = toEmail(entity.email),
            phoneNumber = toPhoneNumber(entity.phoneNumber),
            address = toAddress(entity),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            version = entity.version
        )
    }

    fun toEntity(domain: Customer): CustomerEntity {
        return CustomerEntity(
            id = domain.id,
            firstName = domain.name.firstName,
            lastName = domain.name.lastName,
            email = domain.email.value,
            phoneNumber = domain.phoneNumber.value,
            zipCode = domain.address.zipCode,
            street = domain.address.street,
            city = domain.address.city,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            version = domain.version
        )
    }

    private fun toCustomerName(entity: CustomerEntity): CustomerName {
        return CustomerName(entity.firstName, entity.lastName)
    }

    private fun toEmail(email: String): Email {
        return Email(email)
    }

    private fun toPhoneNumber(phoneNumber: String): PhoneNumber {
        return PhoneNumber(phoneNumber)
    }

    private fun toAddress(entity: CustomerEntity): Address {
        return Address(entity.zipCode, entity.street, entity.city)
    }
}