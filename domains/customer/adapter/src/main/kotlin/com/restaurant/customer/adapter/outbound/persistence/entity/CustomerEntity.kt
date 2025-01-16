package com.restaurant.customer.adapter.outbound.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "customers")
class CustomerEntity(
    @Id
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, unique = true)
    var phoneNumber: String,

    @Column(nullable = false)
    var zipCode: String,

    @Column(nullable = false)
    var street: String,

    @Column(nullable = false)
    var city: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false)
    var updatedAt: LocalDateTime,

    @Version
    @Column(nullable = false)
    var version: Long = 0
)