package com.restaurant.customer.domain.repository.entities

import jakarta.persistence.*

@Entity
class Customer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val email: String,
    @OneToOne(cascade = [CascadeType.ALL])
    val customerInfo: CustomerInfo,
    @Enumerated(EnumType.STRING)
    val customerGrade: CustomerGrade
) {
    constructor() : this(0, "", "", CustomerInfo(), CustomerGrade.BRONZE)
}