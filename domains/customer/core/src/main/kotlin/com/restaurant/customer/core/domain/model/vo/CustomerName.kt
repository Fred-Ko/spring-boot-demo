package com.restaurant.customer.core.domain.model.vo

data class CustomerName(
    val firstName: String,
    val lastName: String
) {
    init {
        require(firstName.isNotBlank()) { "성은 비어있을 수 없습니다." }
        require(lastName.isNotBlank()) { "이름은 비어있을 수 없습니다." }
        require(firstName.length <= 50) { "성은 50자를 초과할 수 없습니다." }
        require(lastName.length <= 50) { "이름은 50자를 초과할 수 없습니다." }
    }

    override fun toString(): String = "$lastName $firstName"
}