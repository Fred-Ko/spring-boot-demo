package com.restaurant.customer.adapter.outbound.event

import com.ddd.outbox.core.application.service.OutboxService
import com.restaurant.customer.core.domain.model.*
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CustomerEventListener(
    private val outboxService: OutboxService
) {
    private val logger = org.slf4j.LoggerFactory.getLogger(CustomerEventListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerCreated(event: CustomerCreatedEvent) {
        logger.info("Handling CustomerCreatedEvent: {}", event)
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerCreated",
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
        logger.info("Successfully saved CustomerCreatedEvent to outbox")
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerNameUpdated(event: CustomerNameUpdatedEvent) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerNameUpdated",
            payload = mapOf(
                "customerId" to event.customer.id,
                "firstName" to event.customer.name.firstName,
                "lastName" to event.customer.name.lastName
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerEmailUpdated(event: CustomerEmailUpdatedEvent) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerEmailUpdated",
            payload = mapOf(
                "customerId" to event.customer.id,
                "email" to event.customer.email.value
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerPhoneNumberUpdated(event: CustomerPhoneNumberUpdatedEvent) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerPhoneNumberUpdated",
            payload = mapOf(
                "customerId" to event.customer.id,
                "phoneNumber" to event.customer.phoneNumber.value
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleCustomerAddressUpdated(event: CustomerAddressUpdatedEvent) {
        outboxService.saveEvent(
            aggregateType = "Customer",
            aggregateId = event.customer.id.toString(),
            eventType = "CustomerAddressUpdated",
            payload = mapOf(
                "customerId" to event.customer.id,
                "zipCode" to event.customer.address.zipCode,
                "street" to event.customer.address.street,
                "city" to event.customer.address.city
            )
        )
    }
}