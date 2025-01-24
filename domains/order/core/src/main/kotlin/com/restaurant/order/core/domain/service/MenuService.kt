package com.restaurant.order.core.domain.service

import com.restaurant.order.core.domain.model.Menu
import com.restaurant.order.core.domain.model.vo.Availability
import com.restaurant.order.core.domain.model.vo.Category
import com.restaurant.order.core.domain.model.vo.Price
import com.restaurant.order.core.domain.repository.MenuRepository
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MenuService(private val menuRepository: MenuRepository) {
    @Transactional
    fun createMenu(
        name: String,
        description: String,
        price: Price,
        availability: Availability,
        category: Category
    ): Menu {
        val menu =
            Menu.create(
                name = name,
                description = description,
                price = price,
                availability = availability,
                category = category
            )
        return menuRepository.save(menu)
    }

    @Transactional(readOnly = true)
    fun getMenu(id: UUID): Menu {
        return menuRepository.findByIdOrThrow(id)
    }


    @Transactional(readOnly = true)
    fun getAllMenus(pageable: Pageable): Page<Menu> {
        return menuRepository.findAll(pageable)
    }

    @Transactional(readOnly = true)
    fun getMenusByCategory(category: Category): List<Menu> {
        return menuRepository.findByCategory(category)
    }


    @Transactional(readOnly = true)
    fun getAvailableMenus(pageable: Pageable): Page<Menu> {
        return menuRepository.findAvailable(pageable)
    }

    @Transactional
    fun updatePrice(id: UUID, newPrice: Price): Menu {
        val menu = menuRepository.findByIdOrThrow(id)
        menu.updatePrice(newPrice)
        return menuRepository.save(menu)
    }

    @Transactional
    fun updateAvailability(id: UUID, newAvailability: Availability): Menu {
        val menu = menuRepository.findByIdOrThrow(id)
        menu.updateAvailability(newAvailability)
        return menuRepository.save(menu)
    }

    @Transactional
    fun decreaseStock(id: UUID, quantity: Int): Menu {
        val menu = menuRepository.findByIdOrThrow(id)
        require(menu.isAvailable()) { "메뉴를 현재 이용할 수 없습니다." }
        require(menu.hasEnoughStock(quantity)) { "재고가 부족합니다." }
        menu.decreaseStock(quantity)
        return menuRepository.save(menu)
    }

    @Transactional
    fun deleteMenu(id: UUID) {
        val menu = menuRepository.findByIdOrThrow(id)
        menuRepository.delete(menu)
    }

    @Transactional(readOnly = true)
    fun existsMenu(id: UUID): Boolean {
        return menuRepository.existsById(id)
    }

    @Transactional
    fun save(menu: Menu): Menu {
        return menuRepository.save(menu)
    }
}
