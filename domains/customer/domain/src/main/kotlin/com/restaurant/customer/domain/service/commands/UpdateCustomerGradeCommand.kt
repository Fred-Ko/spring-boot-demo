package com.restaurant.customer.domain.service.commands

data class UpdateCustomerGradeCommand(
    val id: Long,
    val grade: String
) 