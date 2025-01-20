package com.restaurant.customer.adapter.inbound.api.query

import com.restaurant.customer.core.application.service.CustomerApplicationService
import com.restaurant.customer.core.application.service.query.CustomerResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/customers")
class CustomerQueryController(
    private val customerApplicationService: CustomerApplicationService
) {

    @GetMapping("/{customerId}")
    fun getCustomer(@PathVariable customerId: UUID): ResponseEntity<CustomerResponse> {
        return customerApplicationService.getCustomer(customerId)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/by-email")
    fun findCustomerByEmail(@RequestParam email: String): ResponseEntity<CustomerResponse> {
        return customerApplicationService.findCustomerByEmail(email)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/by-phone")
    fun findCustomerByPhoneNumber(@RequestParam phoneNumber: String): ResponseEntity<CustomerResponse> {
        return customerApplicationService.findCustomerByPhoneNumber(phoneNumber)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @GetMapping
    fun findAllCustomers(pageable: Pageable): ResponseEntity<Page<CustomerResponse>> {
        return customerApplicationService.findAllCustomers(pageable)
            .let { ResponseEntity.ok(it) }
    }
}