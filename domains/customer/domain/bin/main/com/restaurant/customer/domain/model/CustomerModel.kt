package com.restaurant.customer.domain.model

import com.example.customer.domain.CustomerGrade

data class CustomerModel(
    val id: Long = 0,
    val name: String,
    val email: String,
    val customerInfo: CustomerInfoModel,
    val customerGrade: CustomerGrade
) 