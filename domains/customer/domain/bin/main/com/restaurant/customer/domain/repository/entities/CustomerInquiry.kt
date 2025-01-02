package com.example.customer.domain

import javax.persistence.*

@Entity
data class CustomerInquiry(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val inquiryDetails: String,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: Customer
) 