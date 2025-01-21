package com.restaurant.customer.core.domain.event

import com.restaurant.customer.core.domain.model.Customer

sealed class CustomerEvent(
    val customer: Customer,
    val version: Long = 1,
    val topic: String = "com.restaurant.customer"
)

class CustomerCreatedEventV1(customer: Customer) : CustomerEvent(customer)
class CustomerNameUpdatedEventV1(customer: Customer) : CustomerEvent(customer)
class CustomerEmailUpdatedEventV1(customer: Customer) : CustomerEvent(customer)
class CustomerPhoneNumberUpdatedEventV1(customer: Customer) : CustomerEvent(customer)
class CustomerAddressUpdatedEventV1(customer: Customer) : CustomerEvent(customer)