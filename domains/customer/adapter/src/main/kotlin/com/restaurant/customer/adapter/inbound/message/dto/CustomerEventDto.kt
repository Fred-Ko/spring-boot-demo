package com.restaurant.customer.adapter.inbound.message.dto

import com.restaurant.customer.core.domain.event.*
import com.restaurant.customer.core.domain.model.*
import com.restaurant.customer.core.domain.model.vo.*
import java.time.LocalDateTime
import java.util.UUID

data class EventEnvelope(
        val id: UUID,
        val aggregateType: String,
        val aggregateId: UUID,
        val eventType: String,
        val version: Long,
        val timestamp: List<Int>,
        val payload: Map<String, Any>
)

sealed interface CustomerEventDto {
    fun toDomainEvent(): CustomerEvent
}

data class CustomerCreatedEventDto(
        val customerId: UUID,
        val firstName: String,
        val lastName: String,
        val email: String,
        val phoneNumber: String,
        val zipCode: String,
        val street: String,
        val city: String,
        val eventType: String
) : CustomerEventDto {
    override fun toDomainEvent() =
            CustomerCreatedEventV1(
                    Customer.from(
                            id = customerId,
                            name = CustomerName(firstName, lastName),
                            email = Email(email),
                            phoneNumber = PhoneNumber(phoneNumber),
                            address = Address(zipCode, street, city),
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now(),
                            version = 0
                    )
            )
}

data class CustomerEmailUpdatedEventDto(
        val customerId: UUID,
        val email: String,
        val eventType: String
) : CustomerEventDto {
    override fun toDomainEvent() =
            CustomerEmailUpdatedEventV1(
                    Customer.forEmailUpdate(id = customerId, email = Email(email))
            )
}

data class CustomerPhoneNumberUpdatedEventDto(
        val customerId: UUID,
        val phoneNumber: String,
        val eventType: String
) : CustomerEventDto {
    override fun toDomainEvent() =
            CustomerPhoneNumberUpdatedEventV1(
                    Customer.forPhoneNumberUpdate(
                            id = customerId,
                            phoneNumber = PhoneNumber(phoneNumber)
                    )
            )
}

data class CustomerAddressUpdatedEventDto(
        val customerId: UUID,
        val zipCode: String,
        val street: String,
        val city: String,
        val eventType: String
) : CustomerEventDto {
    override fun toDomainEvent() =
            CustomerAddressUpdatedEventV1(
                    Customer.forAddressUpdate(
                            id = customerId,
                            address = Address(zipCode, street, city)
                    )
            )
}
