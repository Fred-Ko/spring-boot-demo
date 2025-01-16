package com.restaurant.customer.core.domain.model.vo

data class PhoneNumber(val value: String) {
    init {
        require(value.matches(PHONE_REGEX)) { "유효하지 않은 전화번호 형식입니다." }
    }

    companion object {
        private val PHONE_REGEX = Regex(
            "^\\d{3}-\\d{3,4}-\\d{4}$"
        )
    }

    override fun toString(): String = value
}