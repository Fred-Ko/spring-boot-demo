package com.restaurant.customer.domain.repository.entities

import jakarta.persistence.*

@Entity
class CustomerInquiry(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val inquiryDetails: String,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: Customer
) {
    constructor() : this(0, "", Customer(0, "", "", CustomerInfo(), CustomerGrade.BRONZE))
}