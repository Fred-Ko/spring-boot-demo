package com.restaurant.customer.core.domain.model

import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import java.time.LocalDateTime
import java.util.*

class Customer private constructor(
    val id: UUID,
    var name: CustomerName,
    var email: Email,
    var phoneNumber: PhoneNumber,
    var address: Address,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var version: Long = 0
) {
    init {
        require(id != UUID(0, 0)) { "고객 ID는 필수입니다." }
    }

    companion object {
        fun create(
            name: CustomerName,
            email: Email,
            phoneNumber: PhoneNumber,
            address: Address
        ): Customer {
            val now = LocalDateTime.now()
            return Customer(
                id = UUID.randomUUID(),
                name = name,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                createdAt = now,
                updatedAt = now,
                version = 0
            )
        }
    }

    fun updateName(newName: CustomerName) {
        this.name = newName
        this.updatedAt = LocalDateTime.now()
    }

    fun updateEmail(newEmail: Email) {
        this.email = newEmail
        this.updatedAt = LocalDateTime.now()
    }

    fun updatePhoneNumber(newPhoneNumber: PhoneNumber) {
        this.phoneNumber = newPhoneNumber
        this.updatedAt = LocalDateTime.now()
    }

    fun updateAddress(newAddress: Address) {
        this.address = newAddress
        this.updatedAt = LocalDateTime.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customer
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}