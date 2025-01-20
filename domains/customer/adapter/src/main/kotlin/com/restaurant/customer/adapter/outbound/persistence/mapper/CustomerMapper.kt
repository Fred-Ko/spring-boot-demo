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
            name = CustomerName(entity.firstName, entity.lastName),
            email = Email(entity.email),
            phoneNumber = PhoneNumber(entity.phoneNumber),
            address = Address(entity.zipCode, entity.street, entity.city),
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
}