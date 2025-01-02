package com.example.customer.domain

import javax.persistence.*

@Entity
data class CustomerInfo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val address: String,
    val phoneNumber: String
) 