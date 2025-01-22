package com.restaurant.order.adapter.inbound.api.request

import com.restaurant.order.core.application.service.command.CreateMenuCommand
import com.restaurant.order.core.application.service.command.UpdateMenuCommand
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalTime

data class CreateMenuRequest(
        @field:NotBlank(message = "메뉴 이름은 필수입니다.") val name: String,
        @field:NotBlank(message = "메뉴 설명은 필수입니다.") val description: String,
        @field:NotNull(message = "가격은 필수입니다.")
        @field:Min(0, message = "가격은 0 이상이어야 합니다.")
        val price: BigDecimal,
        @field:NotNull(message = "카테고리 정보는 필수입니다.") val category: CategoryRequest,
        @field:NotNull(message = "가용성 정보는 필수입니다.") val availability: AvailabilityRequest
) {
        fun toCommand() =
                CreateMenuCommand(
                        name = name,
                        description = description,
                        price = price,
                        category = category.toCreateCommand(),
                        availability = availability.toCreateCommand()
                )
}

data class UpdateMenuRequest(
        val name: String? = null,
        val description: String? = null,
        val price: BigDecimal? = null,
        val category: CategoryRequest? = null,
        val availability: AvailabilityRequest? = null
) {
        fun toCommand() =
                UpdateMenuCommand(
                        name = name,
                        description = description,
                        price = price,
                        category = category?.toUpdateCommand(),
                        availability = availability?.toUpdateCommand()
                )
}

data class CategoryRequest(
        @field:NotBlank(message = "카테고리 이름은 필수입니다.") val name: String,
        val description: String? = null
) {
        fun toCreateCommand() =
                CreateMenuCommand.CategoryInfo(name = name, description = description)
        fun toUpdateCommand() =
                UpdateMenuCommand.CategoryInfo(name = name, description = description)
}

data class AvailabilityRequest(
        @field:NotNull(message = "재고 수량은 필수입니다.")
        @field:Min(0, message = "재고 수량은 0 이상이어야 합니다.")
        val stockQuantity: Int,
        @field:NotNull(message = "운영 시작 시간은 필수입니다.") val operatingStartTime: LocalTime,
        @field:NotNull(message = "운영 종료 시간은 필수입니다.") val operatingEndTime: LocalTime,
        val isAvailable: Boolean = true
) {
        fun toCreateCommand() =
                CreateMenuCommand.AvailabilityInfo(
                        stockQuantity = stockQuantity,
                        operatingStartTime = operatingStartTime,
                        operatingEndTime = operatingEndTime,
                        isAvailable = isAvailable
                )

        fun toUpdateCommand() =
                UpdateMenuCommand.AvailabilityInfo(
                        stockQuantity = stockQuantity,
                        operatingStartTime = operatingStartTime,
                        operatingEndTime = operatingEndTime,
                        isAvailable = isAvailable
                )
}
