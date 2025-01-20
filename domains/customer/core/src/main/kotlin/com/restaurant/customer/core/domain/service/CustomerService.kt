package com.restaurant.customer.core.domain.service

import com.restaurant.customer.core.domain.model.Customer
import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import com.restaurant.customer.core.domain.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) {

    @Transactional
    fun createCustomer(
        name: CustomerName,
        email: Email,
        phoneNumber: PhoneNumber,
        address: Address
    ): Customer {
        validateNewCustomer(email, phoneNumber)
        return Customer.create(name, email, phoneNumber, address)
            .also { customerRepository.save(it) }
    }

    @Transactional
    fun updateCustomerInfo(
        customerId: UUID,
        name: CustomerName? = null,
        email: Email? = null,
        phoneNumber: PhoneNumber? = null,
        address: Address? = null
    ): Customer {
        val customer = getCustomerById(customerId)

        email?.let {
            validateEmailUpdate(customer, it)
            customer.updateEmail(it)
        }

        phoneNumber?.let {
            validatePhoneNumberUpdate(customer, it)
            customer.updatePhoneNumber(it)
        }

        name?.let { customer.updateName(it) }
        address?.let { customer.updateAddress(it) }

        return customerRepository.save(customer)
    }

    @Transactional
    fun deleteCustomer(customerId: UUID) {
        val customer = getCustomerById(customerId)
        customerRepository.delete(customer)
    }

    @Transactional(readOnly = true)
    fun getCustomerById(customerId: UUID): Customer {
        return customerRepository.findById(customerId)
            ?: throw NoSuchElementException("고객을 찾을 수 없습니다. (ID: $customerId)")
    }

    @Transactional(readOnly = true)
    fun findCustomerByEmail(email: Email): Customer? {
        return customerRepository.findByEmail(email)
    }

    @Transactional(readOnly = true)
    fun findCustomerByPhoneNumber(phoneNumber: PhoneNumber): Customer? {
        return customerRepository.findByPhoneNumber(phoneNumber)
    }

    @Transactional(readOnly = true)
    fun findAllCustomers(pageable: Pageable): Page<Customer> {
        return customerRepository.findAll(pageable)
    }

    private fun validateNewCustomer(email: Email, phoneNumber: PhoneNumber) {
        require(!customerRepository.existsByEmail(email)) { "이미 사용 중인 이메일입니다." }
        require(!customerRepository.existsByPhoneNumber(phoneNumber)) { "이미 사용 중인 전화번호입니다." }
    }

    private fun validateEmailUpdate(customer: Customer, newEmail: Email) {
        if (customer.email != newEmail) {
            require(!customerRepository.existsByEmail(newEmail)) { "이미 사용 중인 이메일입니다." }
        }
    }

    private fun validatePhoneNumberUpdate(customer: Customer, newPhoneNumber: PhoneNumber) {
        if (customer.phoneNumber != newPhoneNumber) {
            require(!customerRepository.existsByPhoneNumber(newPhoneNumber)) { "이미 사용 중인 전화번호입니다." }
        }
    }
}