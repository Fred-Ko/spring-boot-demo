package com.restaurant.customer.presentation.restapi

import com.restaurant.customer.presentation.mapper.CustomerMapper
import com.restaurant.customer.domain.service.commands.CreateCustomerCommand
import com.restaurant.customer.domain.service.commands.CreateCustomerInquiryCommand
import com.restaurant.customer.domain.service.commands.UpdateCustomerCommand
import com.restaurant.customer.domain.service.commands.DeleteCustomerCommand
import com.restaurant.customer.domain.service.commands.UpdateCustomerGradeCommand
import com.restaurant.customer.domain.service.handlers.CustomerCommandHandler
import com.restaurant.customer.domain.service.handlers.CustomerQueryHandler
import com.restaurant.customer.presentation.dto.CustomerDto
import com.restaurant.customer.presentation.dto.CustomerGradeDto
import com.restaurant.customer.presentation.dto.CustomerInquiryDto
import com.restaurant.customer.presentation.queries.GetCustomerGradeQuery
import com.restaurant.customer.presentation.queries.GetCustomerInquiriesQuery
import com.restaurant.customer.presentation.queries.GetCustomerQuery
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerCommandHandler: CustomerCommandHandler,
    private val customerQueryHandler: CustomerQueryHandler
) {

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: Long): CustomerDto {
        val query = GetCustomerQuery(id)
        val customerModel = customerQueryHandler.handle(query)
        return CustomerMapper.toDto(customerModel)
    }

    @PostMapping
    fun createCustomer(@RequestBody customerDto: CustomerDto) {
        val command = CreateCustomerCommand(customerDto.name, customerDto.email)
        customerCommandHandler.handle(command)
    }

    @PutMapping("/{id}")
    fun updateCustomer(@PathVariable id: Long, @RequestBody customerDto: CustomerDto) {
        val command = UpdateCustomerCommand(id, customerDto.name, customerDto.email)
        customerCommandHandler.handle(command)
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long) {
        val command = DeleteCustomerCommand(id)
        customerCommandHandler.handle(command)
    }

    @GetMapping("/{id}/grade")
    fun getCustomerGrade(@PathVariable id: Long): CustomerGradeDto {
        val query = GetCustomerGradeQuery(id)
        val customerGradeModel = customerQueryHandler.handle(query)
        return CustomerMapper.toDto(customerGradeModel)
    }

    @PutMapping("/{id}/grade")
    fun updateCustomerGrade(@PathVariable id: Long, @RequestBody gradeDto: CustomerGradeDto) {
        val command = UpdateCustomerGradeCommand(id, gradeDto.grade)
        customerCommandHandler.handle(command)
    }

    @PostMapping("/{id}/inquiries")
    fun createCustomerInquiry(@PathVariable id: Long, @RequestBody inquiryDto: CustomerInquiryDto) {
        val command = CreateCustomerInquiryCommand(id, inquiryDto.message)
        customerCommandHandler.handle(command)
    }

    @GetMapping("/{id}/inquiries")
    fun getCustomerInquiries(@PathVariable id: Long): List<CustomerInquiryDto> {
        val query = GetCustomerInquiriesQuery(id)
        val inquiryModels = customerQueryHandler.handle(query)
        return inquiryModels.map { CustomerMapper.toDto(it) }
    }
}