package com.restaurant.order.adapter.outbound.event

import com.ddd.outbox.core.application.service.OutboxService
import com.restaurant.order.core.domain.event.*
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class MenuEventListener(private val outboxService: OutboxService) {
        @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
        fun handleMenuCreated(event: MenuCreatedEventV1) {
                outboxService.saveEvent(
                        aggregateType = "menu",
                        aggregateId = event.menu.id.toString(),
                        eventType = "MenuCreated",
                        version = event.menu.version,
                        topic = "menu",
                        payload =
                                mapOf(
                                        "menuId" to event.menu.id,
                                        "name" to event.menu.name,
                                        "description" to event.menu.description,
                                        "price" to event.menu.getPrice().amount,
                                        "category" to
                                                mapOf(
                                                        "name" to event.menu.category.name,
                                                        "description" to
                                                                event.menu.category.description
                                                ),
                                        "availability" to
                                                mapOf(
                                                        "stockQuantity" to
                                                                event.menu
                                                                        .availability
                                                                        .stockQuantity,
                                                        "operatingStartTime" to
                                                                event.menu
                                                                        .availability
                                                                        .operatingHours
                                                                        .startTime,
                                                        "operatingEndTime" to
                                                                event.menu
                                                                        .availability
                                                                        .operatingHours
                                                                        .endTime,
                                                        "isAvailable" to event.menu.isAvailable()
                                                )
                                )
                )
        }

        @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
        fun handleMenuPriceUpdated(event: MenuPriceUpdatedEventV1) {
                outboxService.saveEvent(
                        aggregateType = "menu",
                        aggregateId = event.menu.id.toString(),
                        eventType = "MenuPriceUpdated",
                        version = event.menu.version,
                        topic = "menu",
                        payload =
                                mapOf(
                                        "menuId" to event.menu.id,
                                        "price" to event.menu.getPrice().amount
                                )
                )
        }

        @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
        fun handleMenuAvailabilityUpdated(event: MenuAvailabilityUpdatedEventV1) {
                outboxService.saveEvent(
                        aggregateType = "menu",
                        aggregateId = event.menu.id.toString(),
                        eventType = "MenuAvailabilityUpdated",
                        version = event.menu.version,
                        topic = "menu",
                        payload =
                                mapOf(
                                        "menuId" to event.menu.id,
                                        "availability" to
                                                mapOf(
                                                        "stockQuantity" to
                                                                event.menu
                                                                        .availability
                                                                        .stockQuantity,
                                                        "operatingStartTime" to
                                                                event.menu
                                                                        .availability
                                                                        .operatingHours
                                                                        .startTime,
                                                        "operatingEndTime" to
                                                                event.menu
                                                                        .availability
                                                                        .operatingHours
                                                                        .endTime,
                                                        "isAvailable" to event.menu.isAvailable()
                                                )
                                )
                )
        }

        @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
        fun handleMenuStockDecreased(event: MenuStockDecreasedEventV1) {
                outboxService.saveEvent(
                        aggregateType = "menu",
                        aggregateId = event.menu.id.toString(),
                        eventType = "MenuStockDecreased",
                        version = event.menu.version,
                        topic = "menu",
                        payload =
                                mapOf(
                                        "menuId" to event.menu.id,
                                        "quantity" to event.decreasedQuantity
                                )
                )
        }
}
