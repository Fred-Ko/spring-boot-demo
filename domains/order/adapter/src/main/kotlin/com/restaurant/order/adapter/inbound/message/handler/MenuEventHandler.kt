package com.restaurant.order.adapter.inbound.message.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.restaurant.order.adapter.inbound.message.dto.*
import com.restaurant.order.core.domain.event.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MenuEventHandler(private val objectMapper: ObjectMapper) {
  private val log = LoggerFactory.getLogger(this::class.java)

  fun handle(message: String) {
    log.debug("Received message: {}", message)

    // 메시지를 EventEnvelope로 파싱
    val envelope = objectMapper.readValue<EventEnvelope>(message)

    // payload와 eventType을 결합하여 새로운 맵 생성
    val eventData = envelope.payload.toMutableMap().apply { put("eventType", envelope.eventType) }

    // 이벤트 타입에 따라 적절한 DTO로 변환
    val eventDto =
            when (envelope.eventType) {
              "MenuCreated" -> objectMapper.convertValue(eventData, MenuCreatedEventDto::class.java)
              "MenuPriceUpdated" ->
                      objectMapper.convertValue(eventData, MenuPriceUpdatedEventDto::class.java)
              "MenuAvailabilityUpdated" ->
                      objectMapper.convertValue(
                              eventData,
                              MenuAvailabilityUpdatedEventDto::class.java
                      )
              "MenuStockDecreased" ->
                      objectMapper.convertValue(eventData, MenuStockDecreasedEventDto::class.java)
              else -> throw IllegalArgumentException("Unknown event type: ${envelope.eventType}")
            }

    val event = eventDto.toDomainEvent()

    when (event) {
      is MenuCreatedEventV1 -> handleMenuCreated(event)
      is MenuPriceUpdatedEventV1 -> handleMenuPriceUpdated(event)
      is MenuAvailabilityUpdatedEventV1 -> handleMenuAvailabilityUpdated(event)
      is MenuStockDecreasedEventV1 -> handleMenuStockDecreased(event)
    }
  }

  private fun handleMenuCreated(event: MenuCreatedEventV1) {
    log.info(
            "Handling MenuCreatedEvent - Menu ID: {}, Name: {}, Price: {}, Category: {}",
            event.menu.id,
            event.menu.name,
            event.menu.getPrice(),
            event.menu.category
    )
  }

  private fun handleMenuPriceUpdated(event: MenuPriceUpdatedEventV1) {
    log.info(
            "Handling MenuPriceUpdatedEvent - Menu ID: {}, New Price: {}",
            event.menu.id,
            event.menu.getPrice()
    )
  }

  private fun handleMenuAvailabilityUpdated(event: MenuAvailabilityUpdatedEventV1) {
    log.info(
            "Handling MenuAvailabilityUpdatedEvent - Menu ID: {}, Is Available: {}",
            event.menu.id,
            event.menu.isAvailable()
    )
  }

  private fun handleMenuStockDecreased(event: MenuStockDecreasedEventV1) {
    log.info(
            "Handling MenuStockDecreasedEvent - Menu ID: {}, Decreased Quantity: {}",
            event.menu.id,
            event.decreasedQuantity
    )
  }
}
