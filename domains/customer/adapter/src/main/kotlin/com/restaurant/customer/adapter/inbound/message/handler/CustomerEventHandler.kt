package com.restaurant.customer.adapter.inbound.message.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.restaurant.customer.adapter.inbound.message.dto.CustomerAddressUpdatedEventDto
import com.restaurant.customer.adapter.inbound.message.dto.CustomerCreatedEventDto
import com.restaurant.customer.adapter.inbound.message.dto.CustomerEmailUpdatedEventDto
import com.restaurant.customer.adapter.inbound.message.dto.CustomerPhoneNumberUpdatedEventDto
import com.restaurant.customer.adapter.inbound.message.dto.EventEnvelope
import com.restaurant.customer.core.domain.model.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CustomerEventHandler(private val objectMapper: ObjectMapper) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun handle(message: String) {
        log.debug("Received message: {}", message)

        // 메시지를 EventEnvelope로 파싱
        val envelope = objectMapper.readValue<EventEnvelope>(message)

        // payload와 eventType을 결합하여 새로운 맵 생성
        val eventData =
                envelope.payload.toMutableMap().apply { put("eventType", envelope.eventType) }

        // 이벤트 타입에 따라 적절한 DTO로 변환
        val eventDto =
                when (envelope.eventType) {
                    "CustomerCreated" ->
                            objectMapper.convertValue(
                                    eventData,
                                    CustomerCreatedEventDto::class.java
                            )
                    "CustomerEmailUpdated" ->
                            objectMapper.convertValue(
                                    eventData,
                                    CustomerEmailUpdatedEventDto::class.java
                            )
                    "CustomerPhoneNumberUpdated" ->
                            objectMapper.convertValue(
                                    eventData,
                                    CustomerPhoneNumberUpdatedEventDto::class.java
                            )
                    "CustomerAddressUpdated" ->
                            objectMapper.convertValue(
                                    eventData,
                                    CustomerAddressUpdatedEventDto::class.java
                            )
                    else ->
                            throw IllegalArgumentException(
                                    "Unknown event type: ${envelope.eventType}"
                            )
                }

        val event = eventDto.toDomainEvent()

        when (event) {
            is CustomerCreatedEventV1 -> handleCustomerCreated(event)
            is CustomerNameUpdatedEventV1 -> handleCustomerNameUpdated(event)
            is CustomerEmailUpdatedEventV1 -> handleCustomerEmailUpdated(event)
            is CustomerPhoneNumberUpdatedEventV1 -> handleCustomerPhoneNumberUpdated(event)
            is CustomerAddressUpdatedEventV1 -> handleCustomerAddressUpdated(event)
        }
    }

    private fun handleCustomerCreated(event: CustomerCreatedEventV1) {
        log.info(
                "Handling CustomerCreatedEvent - Customer ID: {}, Name: {}, Email: {}, Phone: {}, Address: {}",
                event.customer.id,
                event.customer.name,
                event.customer.email,
                event.customer.phoneNumber,
                event.customer.address
        )
    }

    private fun handleCustomerNameUpdated(event: CustomerNameUpdatedEventV1) {
        log.info(
                "Handling CustomerNameUpdatedEvent - Customer ID: {}, New Name: {}",
                event.customer.id,
                event.customer.name
        )
    }

    private fun handleCustomerEmailUpdated(event: CustomerEmailUpdatedEventV1) {
        log.info(
                "Handling CustomerEmailUpdatedEvent - Customer ID: {}, New Email: {}",
                event.customer.id,
                event.customer.email
        )
    }

    private fun handleCustomerPhoneNumberUpdated(event: CustomerPhoneNumberUpdatedEventV1) {
        log.info(
                "Handling CustomerPhoneNumberUpdatedEvent - Customer ID: {}, New Phone: {}",
                event.customer.id,
                event.customer.phoneNumber
        )
    }

    private fun handleCustomerAddressUpdated(event: CustomerAddressUpdatedEventV1) {
        log.info(
                "Handling CustomerAddressUpdatedEvent - Customer ID: {}, New Address: {}",
                event.customer.id,
                event.customer.address
        )
    }
}
