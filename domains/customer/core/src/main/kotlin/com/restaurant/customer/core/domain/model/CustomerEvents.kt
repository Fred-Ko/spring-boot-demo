package com.restaurant.customer.core.domain.model

sealed class CustomerEvent(val customer: Customer)

class CustomerCreatedEvent(customer: Customer) : CustomerEvent(customer)
class CustomerNameUpdatedEvent(customer: Customer) : CustomerEvent(customer)
class CustomerEmailUpdatedEvent(customer: Customer) : CustomerEvent(customer)
class CustomerPhoneNumberUpdatedEvent(customer: Customer) : CustomerEvent(customer)
class CustomerAddressUpdatedEvent(customer: Customer) : CustomerEvent(customer)