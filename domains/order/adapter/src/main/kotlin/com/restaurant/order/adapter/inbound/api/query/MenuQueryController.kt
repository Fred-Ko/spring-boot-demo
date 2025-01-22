package com.restaurant.order.adapter.inbound.api.query

import com.restaurant.order.core.application.service.MenuApplicationService
import com.restaurant.order.core.application.service.query.MenuResponse
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/menus")
class MenuQueryController(private val menuApplicationService: MenuApplicationService) {
  @GetMapping("/{menuId}")
  fun getMenu(@PathVariable menuId: UUID): ResponseEntity<MenuResponse> {
    return menuApplicationService.getMenu(menuId).let { ResponseEntity.ok(it) }
  }

  @GetMapping
  fun findAllMenus(pageable: Pageable): ResponseEntity<Page<MenuResponse>> {
    return menuApplicationService.findAllMenus(pageable).let { ResponseEntity.ok(it) }
  }

  @GetMapping("/available")
  fun findAvailableMenus(pageable: Pageable): ResponseEntity<Page<MenuResponse>> {
    return menuApplicationService.findAvailableMenus(pageable).let { ResponseEntity.ok(it) }
  }

  @GetMapping("/category/{categoryName}")
  fun findMenusByCategory(
          @PathVariable categoryName: String,
          pageable: Pageable
  ): ResponseEntity<Page<MenuResponse>> {
    return menuApplicationService.findMenusByCategory(categoryName, pageable).let {
      ResponseEntity.ok(it)
    }
  }
}
