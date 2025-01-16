package com.restaurant.customer.core.domain.model.vo

data class Address(
    val zipCode: String,
    val address1: String,
    val address2: String
) {
    init {
        require(zipCode.matches(ZIPCODE_REGEX)) { "유효하지 않은 우편번호 형식입니다." }
        require(address1.isNotBlank()) { "기본 주소는 비어있을 수 없습니다." }
        require(address1.length <= 100) { "기본 주소는 100자를 초과할 수 없습니다." }
        require(address2.length <= 100) { "상세 주소는 100자를 초과할 수 없습니다." }
    }

    companion object {
        private val ZIPCODE_REGEX = Regex("^\\d{5}$")
    }

    override fun toString(): String = "($zipCode) $address1 $address2"
}