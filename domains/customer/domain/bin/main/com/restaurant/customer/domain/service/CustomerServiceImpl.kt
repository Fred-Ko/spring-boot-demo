package com.restaurant.customer.domain.service

import ICustomerService
import com.example.customer.domain.Customer
import com.restaurant.customer.domain.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository) : ICustomerService {
    override fun createCustomer(customer: Customer): Customer {
        return customerRepository.save(customer)
    }

    override fun getCustomerById(id: Long): Customer? {
        return customerRepository.findById(id).orElse(null)
    }
}