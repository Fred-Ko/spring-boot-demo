package com.restaurant.customer.domain.service.commands

data class UpdateCustomerCommand(
    val id: Long,
    val name: String,
    val email: String
) 