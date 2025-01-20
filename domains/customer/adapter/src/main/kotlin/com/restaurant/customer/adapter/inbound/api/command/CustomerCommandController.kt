package com.restaurant.customer.adapter.inbound.api.command

import com.restaurant.customer.adapter.inbound.api.request.CreateCustomerRequest
import com.restaurant.customer.adapter.inbound.api.request.UpdateCustomerRequest
import com.restaurant.customer.core.application.service.CustomerApplicationService
import com.restaurant.customer.core.application.service.query.CustomerResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api/v1/customers")
class CustomerCommandController(
    private val customerApplicationService: CustomerApplicationService
) {

    @PostMapping
    fun createCustomer(
        @Valid @RequestBody request: CreateCustomerRequest
    ): ResponseEntity<CustomerResponse> {
        val response = customerApplicationService.createCustomer(request.toCommand())
        return ResponseEntity
            .created(URI.create("/api/v1/customers/${response.id}"))
            .body(response)
    }

    @PutMapping("/{customerId}")
    fun updateCustomer(
        @PathVariable customerId: UUID,
        @Valid @RequestBody request: UpdateCustomerRequest
    ): ResponseEntity<CustomerResponse> {
        val response = customerApplicationService.updateCustomer(customerId, request.toCommand())
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{customerId}")
    fun deleteCustomer(@PathVariable customerId: UUID): ResponseEntity<Unit> {
        customerApplicationService.deleteCustomer(customerId)
        return ResponseEntity.noContent().build()
    }
}