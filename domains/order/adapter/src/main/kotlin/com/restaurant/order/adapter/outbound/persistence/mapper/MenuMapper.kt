package com.restaurant.order.adapter.outbound.persistence.mapper

import com.restaurant.order.adapter.outbound.persistence.entity.AvailabilityEmbeddable
import com.restaurant.order.adapter.outbound.persistence.entity.CategoryEmbeddable
import com.restaurant.order.adapter.outbound.persistence.entity.MenuEntity
import com.restaurant.order.core.domain.model.Menu
import com.restaurant.order.core.domain.model.vo.Availability
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.model.vo.OperatingHours
import com.restaurant.order.core.domain.model.vo.Price
import org.springframework.stereotype.Component

@Component
class MenuMapper {
  fun toDomain(entity: MenuEntity): Menu {
    return Menu.from(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            price = Price(entity.price),
            category =
                    Category(
                            name = entity.category.name,
                            description = entity.category.description
                    ),
            availability =
                    Availability(
                            isAvailable = entity.availability.isAvailable,
                            stockQuantity = entity.availability.stockQuantity,
                            operatingHours =
                                    OperatingHours(
                                            startTime = entity.availability.operatingStartTime,
                                            endTime = entity.availability.operatingEndTime
                                    )
                    ),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            version = entity.version
    )
  }

  fun toEntity(domain: Menu): MenuEntity {
    return MenuEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            price = domain.getPrice().amount,
            category =
                    CategoryEmbeddable(
                            name = domain.category.name,
                            description = domain.category.description
                    ),
            availability =
                    AvailabilityEmbeddable(
                            isAvailable = domain.isAvailable(),
                            stockQuantity = domain.availability.stockQuantity,
                            operatingStartTime = domain.availability.operatingHours.startTime,
                            operatingEndTime = domain.availability.operatingHours.endTime
                    ),
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            version = domain.version
    )
  }
}
