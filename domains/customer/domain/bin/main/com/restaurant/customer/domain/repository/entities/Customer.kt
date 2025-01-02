package com.example.customer.domain

import jakarta.persistence.*

@Entity
data class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val email: String,
    @OneToOne(cascade = [CascadeType.ALL])
    val customerInfo: CustomerInfo,
    @Enumerated(EnumType.STRING)
    val customerGrade: CustomerGrade
) 