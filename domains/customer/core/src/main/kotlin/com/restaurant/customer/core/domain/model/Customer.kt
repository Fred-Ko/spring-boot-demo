package com.restaurant.customer.core.domain.model

import com.restaurant.customer.core.domain.event.*
import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import org.springframework.data.domain.AbstractAggregateRoot
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
) : AbstractAggregateRoot<Customer>() {
    init {
        require(id != UUID(0, 0)) { "고객 ID는 필수입니다." }
    }

    fun getAndClearDomainEvents(): MutableCollection<Any> {
        val events = domainEvents().toMutableList() // 이벤트 복사본 생성
        clearDomainEvents()
        return events
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
            ).also {
                it.registerEvent(CustomerCreatedEventV1(it))
            }
        }

        fun from(
            id: UUID,
            name: CustomerName,
            email: Email,
            phoneNumber: PhoneNumber,
            address: Address,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            version: Long
        ): Customer {
            return Customer(
                id = id,
                name = name,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                createdAt = createdAt,
                updatedAt = updatedAt,
                version = version
            )
        }

        // 이벤트 처리를 위한 팩토리 메서드들
        fun forEmailUpdate(id: UUID, email: Email): Customer {
            val now = LocalDateTime.now()
            return Customer(
                id = id,
                name = CustomerName("", ""),
                email = email,
                phoneNumber = PhoneNumber(""),
                address = Address("", "", ""),
                createdAt = now,
                updatedAt = now,
                version = 0
            )
        }

        fun forPhoneNumberUpdate(id: UUID, phoneNumber: PhoneNumber): Customer {
            val now = LocalDateTime.now()
            return Customer(
                id = id,
                name = CustomerName("", ""),
                email = Email("temp@example.com"),
                phoneNumber = phoneNumber,
                address = Address("", "", ""),
                createdAt = now,
                updatedAt = now,
                version = 0
            )
        }

        fun forAddressUpdate(id: UUID, address: Address): Customer {
            val now = LocalDateTime.now()
            return Customer(
                id = id,
                name = CustomerName("", ""),
                email = Email("temp@example.com"),
                phoneNumber = PhoneNumber(""),
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
        registerEvent(CustomerNameUpdatedEventV1(this))
    }

    fun updateEmail(newEmail: Email) {
        this.email = newEmail
        this.updatedAt = LocalDateTime.now()
        registerEvent(CustomerEmailUpdatedEventV1(this))
    }

    fun updatePhoneNumber(newPhoneNumber: PhoneNumber) {
        this.phoneNumber = newPhoneNumber
        this.updatedAt = LocalDateTime.now()
        registerEvent(CustomerPhoneNumberUpdatedEventV1(this))
    }

    fun updateAddress(newAddress: Address) {
        this.address = newAddress
        this.updatedAt = LocalDateTime.now()
        registerEvent(CustomerAddressUpdatedEventV1(this))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customer
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}