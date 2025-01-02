package com.restaurant.customer.domain.repository.entities

import jakarta.persistence.*

@Entity
class CustomerInfo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val address: String,
    val phoneNumber: String
) {
    constructor() : this(0, "", "")
}