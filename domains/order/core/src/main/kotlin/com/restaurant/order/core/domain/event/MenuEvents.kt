package com.restaurant.order.core.domain.event

import com.restaurant.order.core.domain.model.Menu

sealed class MenuEvent(
        val menu: Menu,
        val version: Long = 1,
        val topic: String = "com.restaurant.order"
)

class MenuCreatedEventV1(menu: Menu) : MenuEvent(menu)

class MenuStockDecreasedEventV1(menu: Menu, val decreasedQuantity: Int) : MenuEvent(menu)

class MenuPriceUpdatedEventV1(menu: Menu) : MenuEvent(menu)

class MenuAvailabilityUpdatedEventV1(menu: Menu) : MenuEvent(menu)
