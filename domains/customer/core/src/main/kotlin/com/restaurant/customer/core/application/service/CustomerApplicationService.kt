package com.restaurant.customer.core.application.service

import com.restaurant.customer.core.application.service.command.CreateCustomerCommand
import com.restaurant.customer.core.application.service.command.UpdateCustomerCommand
import com.restaurant.customer.core.application.service.query.CustomerResponse
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import com.restaurant.customer.core.domain.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CustomerApplicationService(
    private val customerService: CustomerService
) {

    @Transactional
    fun createCustomer(command: CreateCustomerCommand): CustomerResponse {
        val customer = customerService.createCustomer(
            name = command.toCustomerName(),
            email = command.toEmail(),
            phoneNumber = command.toPhoneNumber(),
            address = command.toAddress()
        )
        return CustomerResponse.from(customer)
    }

    @Transactional
    fun updateCustomer(customerId: UUID, command: UpdateCustomerCommand): CustomerResponse {
        val customer = customerService.updateCustomerInfo(
            customerId = customerId,
            name = command.name?.let { command.toCustomerName() },
            email = command.email?.let { command.toEmail() },
            phoneNumber = command.phoneNumber?.let { command.toPhoneNumber() },
            address = command.address?.let { command.toAddress() }
        )
        return CustomerResponse.from(customer)
    }

    @Transactional
    fun deleteCustomer(customerId: UUID) {
        customerService.deleteCustomer(customerId)
    }

    @Transactional(readOnly = true)
    fun getCustomer(customerId: UUID): CustomerResponse {
        val customer = customerService.getCustomerById(customerId)
        return CustomerResponse.from(customer)
    }

    @Transactional(readOnly = true)
    fun findCustomerByEmail(email: String): CustomerResponse? {
        return customerService.findCustomerByEmail(Email(email))
            ?.let { CustomerResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun findCustomerByPhoneNumber(phoneNumber: String): CustomerResponse? {
        return customerService.findCustomerByPhoneNumber(PhoneNumber(phoneNumber))
            ?.let { CustomerResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun findAllCustomers(pageable: Pageable): Page<CustomerResponse> {
        return customerService.findAllCustomers(pageable)
            .map { CustomerResponse.from(it) }
    }
}