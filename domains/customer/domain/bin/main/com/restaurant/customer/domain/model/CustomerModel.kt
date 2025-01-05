package com.restaurant.customer.domain.model

import com.restaurant.customer.domain.repository.entities.CustomerGrade

data class CustomerModel(
    val id: Long = 0,
    val name: String,
    val email: String,
    val customerInfo: CustomerInfoModel,
    val customerGrade: CustomerGrade
) 