package com.restaurant.customer.domain.service.commands

data class CreateCustomerCommand(
    val name: String,
    val email: String
) 