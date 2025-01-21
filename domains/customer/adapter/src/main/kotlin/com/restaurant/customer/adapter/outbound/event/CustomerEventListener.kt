package com.restaurant.customer.adapter.outbound.event

import com.ddd.outbox.core.application.service.OutboxService
import com.restaurant.customer.core.domain.event.*
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CustomerEventListener(
    private val outboxService: OutboxService
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerCreated(event: CustomerCreatedEventV1) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerCreated",
            version = event.version,
            topic = event.topic,
            payload = mapOf(
                "customerId" to event.customer.id,
                "firstName" to event.customer.name.firstName,
                "lastName" to event.customer.name.lastName,
                "email" to event.customer.email.value,
                "phoneNumber" to event.customer.phoneNumber.value,
                "zipCode" to event.customer.address.zipCode,
                "street" to event.customer.address.street,
                "city" to event.customer.address.city
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerNameUpdated(event: CustomerNameUpdatedEventV1) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerNameUpdated",
            version = event.version,
            topic = event.topic,
            payload = mapOf(
                "customerId" to event.customer.id,
                "firstName" to event.customer.name.firstName,
                "lastName" to event.customer.name.lastName
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerEmailUpdated(event: CustomerEmailUpdatedEventV1) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerEmailUpdated",
            version = event.version,
            topic = event.topic,
            payload = mapOf(
                "customerId" to event.customer.id,
                "email" to event.customer.email.value
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerPhoneNumberUpdated(event: CustomerPhoneNumberUpdatedEventV1) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerPhoneNumberUpdated",
            version = event.version,
            topic = event.topic,
            payload = mapOf(
                "customerId" to event.customer.id,
                "phoneNumber" to event.customer.phoneNumber.value
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerAddressUpdated(event: CustomerAddressUpdatedEventV1) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerAddressUpdated",
            version = event.version,
            topic = event.topic,
            payload = mapOf(
                "customerId" to event.customer.id,
                "zipCode" to event.customer.address.zipCode,
                "street" to event.customer.address.street,
                "city" to event.customer.address.city
            )
        )
    }
}