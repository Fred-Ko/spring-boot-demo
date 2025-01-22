package com.restaurant.order.core.application.service

import com.restaurant.order.core.application.service.command.CreateMenuCommand
import com.restaurant.order.core.application.service.command.UpdateMenuCommand
import com.restaurant.order.core.application.service.query.MenuResponse
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.model.vo.Price
import com.restaurant.order.core.domain.service.MenuService
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MenuApplicationService(private val menuService: MenuService) {
  @Transactional
  fun createMenu(command: CreateMenuCommand): MenuResponse {
    val menu =
            menuService.createMenu(
                    name = command.name,
                    description = command.description,
                    price = Price(command.price),
                    availability = command.availability.toDomain(),
                    category =
                            Category(
                                    name = command.category.name,
                                    description = command.category.description
                            )
            )
    return MenuResponse.from(menu)
  }

  @Transactional
  fun updateMenu(id: UUID, command: UpdateMenuCommand): MenuResponse {
    val menu = menuService.getMenu(id)

    command.name?.let { menu.updateName(it) }
    command.description?.let { menu.updateDescription(it) }
    command.price?.let { menu.updatePrice(Price(it)) }
    command.category?.let {
      menu.updateCategory(Category(name = it.name, description = it.description))
    }
    command.availability?.let { menu.updateAvailability(it.toDomain()) }

    return MenuResponse.from(menuService.save(menu))
  }

  @Transactional
  fun deleteMenu(id: UUID) {
    menuService.deleteMenu(id)
  }

  @Transactional
  fun decreaseStock(id: UUID, quantity: Int): MenuResponse {
    val menu = menuService.decreaseStock(id, quantity)
    return MenuResponse.from(menu)
  }

  @Transactional(readOnly = true)
  fun getMenu(id: UUID): MenuResponse {
    val menu = menuService.getMenu(id)
    return MenuResponse.from(menu)
  }

  @Transactional(readOnly = true)
  fun findAllMenus(pageable: Pageable): Page<MenuResponse> {
    return menuService.getAllMenus().map { MenuResponse.from(it) }.let {
      Page.empty()
    } // TODO: 실제 페이징 구현 필요
  }

  @Transactional(readOnly = true)
  fun findAvailableMenus(pageable: Pageable): Page<MenuResponse> {
    return menuService.getAvailableMenus().map { MenuResponse.from(it) }.let {
      Page.empty()
    } // TODO: 실제 페이징 구현 필요
  }

  @Transactional(readOnly = true)
  fun findMenusByCategory(categoryName: String, pageable: Pageable): Page<MenuResponse> {
    return menuService
            .getMenusByCategory(Category(name = categoryName, description = null))
            .map { MenuResponse.from(it) }
            .let { Page.empty() } // TODO: 실제 페이징 구현 필요
  }
}
