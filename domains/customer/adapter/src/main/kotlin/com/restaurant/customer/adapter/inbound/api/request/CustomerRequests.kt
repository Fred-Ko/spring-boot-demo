package com.restaurant.customer.adapter.inbound.api.request

import com.restaurant.customer.core.application.service.command.CreateCustomerCommand
import com.restaurant.customer.core.application.service.command.UpdateCustomerCommand
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CreateCustomerRequest(
    @field:NotBlank(message = "이름은 필수입니다.")
    val firstName: String,

    @field:NotBlank(message = "성은 필수입니다.")
    val lastName: String,

    @field:NotBlank(message = "이메일은 필수입니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    val email: String,

    @field:NotBlank(message = "전화번호는 필수입니다.")
    @field:Pattern(
        regexp = "^\\d{3}-\\d{3,4}-\\d{4}\$",
        message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)"
    )
    val phoneNumber: String,

    @field:NotBlank(message = "우편번호는 필수입니다.")
    val zipCode: String,

    @field:NotBlank(message = "도로명/지번 주소는 필수입니다.")
    val street: String,

    @field:NotBlank(message = "도시는 필수입니다.")
    val city: String
) {
    fun toCommand() = CreateCustomerCommand(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        zipCode = zipCode,
        street = street,
        city = city
    )
}

data class UpdateCustomerRequest(
    val name: NameUpdate? = null,

    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    val email: String? = null,

    @field:Pattern(
        regexp = "^\\d{3}-\\d{3,4}-\\d{4}\$",
        message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)"
    )
    val phoneNumber: String? = null,

    val address: AddressUpdate? = null
) {
    data class NameUpdate(
        @field:NotBlank(message = "이름은 필수입니다.")
        val firstName: String,

        @field:NotBlank(message = "성은 필수입니다.")
        val lastName: String
    )

    data class AddressUpdate(
        @field:NotBlank(message = "우편번호는 필수입니다.")
        val zipCode: String,

        @field:NotBlank(message = "도로명/지번 주소는 필수입니다.")
        val street: String,

        @field:NotBlank(message = "도시는 필수입니다.")
        val city: String
    )

    fun toCommand() = UpdateCustomerCommand(
        name = name?.let { UpdateCustomerCommand.NameUpdate(it.firstName, it.lastName) },
        email = email,
        phoneNumber = phoneNumber,
        address = address?.let {
            UpdateCustomerCommand.AddressUpdate(it.zipCode, it.street, it.city)
        }
    )
}