package com.restaurant.order.adapter.inbound.api.command

import com.restaurant.order.adapter.inbound.api.request.CreateMenuRequest
import com.restaurant.order.adapter.inbound.api.request.UpdateMenuRequest
import com.restaurant.order.core.application.service.MenuApplicationService
import com.restaurant.order.core.application.service.query.MenuResponse
import jakarta.validation.Valid
import java.net.URI
import java.util.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/menus")
class MenuCommandController(private val menuApplicationService: MenuApplicationService) {
  @PostMapping
  fun createMenu(@Valid @RequestBody request: CreateMenuRequest): ResponseEntity<MenuResponse> {
    val response = menuApplicationService.createMenu(request.toCommand())
    return ResponseEntity.created(URI.create("/api/v1/menus/${response.id}")).body(response)
  }

  @PutMapping("/{menuId}")
  fun updateMenu(
          @PathVariable menuId: UUID,
          @Valid @RequestBody request: UpdateMenuRequest
  ): ResponseEntity<MenuResponse> {
    val response = menuApplicationService.updateMenu(menuId, request.toCommand())
    return ResponseEntity.ok(response)
  }

  @DeleteMapping("/{menuId}")
  fun deleteMenu(@PathVariable menuId: UUID): ResponseEntity<Unit> {
    menuApplicationService.deleteMenu(menuId)
    return ResponseEntity.noContent().build()
  }

  @PutMapping("/{menuId}/stock/decrease")
  fun decreaseStock(
          @PathVariable menuId: UUID,
          @RequestParam quantity: Int
  ): ResponseEntity<MenuResponse> {
    val response = menuApplicationService.decreaseStock(menuId, quantity)
    return ResponseEntity.ok(response)
  }
}
