package com.restaurant.customer.core.domain.model.vo

data class Address(
    val zipCode: String,
    val street: String,
    val city: String
) {
    init {
        require(zipCode.matches(ZIPCODE_REGEX)) { "유효하지 않은 우편번호 형식입니다." }
        require(street.isNotBlank()) { "도로명/지번 주소는 비어있을 수 없습니다." }
        require(street.length <= 100) { "도로명/지번 주소는 100자를 초과할 수 없습니다." }
        require(city.isNotBlank()) { "도시는 비어있을 수 없습니다." }
        require(city.length <= 100) { "도시는 100자를 초과할 수 없습니다." }
    }

    companion object {
        private val ZIPCODE_REGEX = Regex("^\\d{5}$")
    }

    override fun toString(): String = "($zipCode) $street, $city"
}