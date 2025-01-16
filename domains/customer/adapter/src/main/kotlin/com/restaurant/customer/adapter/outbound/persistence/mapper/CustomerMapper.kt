package com.restaurant.customer.adapter.outbound.persistence.mapper

import com.restaurant.customer.adapter.outbound.persistence.entity.CustomerEntity
import com.restaurant.customer.core.domain.model.Customer
import com.restaurant.customer.core.domain.model.vo.Address
import com.restaurant.customer.core.domain.model.vo.CustomerName
import com.restaurant.customer.core.domain.model.vo.Email
import com.restaurant.customer.core.domain.model.vo.PhoneNumber
import org.mapstruct.*
import java.util.*

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface CustomerMapper {

    @Mapping(target = "name", expression = "java(toCustomerName(entity))")
    @Mapping(target = "email", expression = "java(toEmail(entity.getEmail()))")
    @Mapping(target = "phoneNumber", expression = "java(toPhoneNumber(entity.getPhoneNumber()))")
    @Mapping(target = "address", expression = "java(toAddress(entity))")
    @Mapping(target = "version", source = "version")
    fun toDomain(entity: CustomerEntity): Customer

    @Mapping(target = "firstName", source = "name.firstName")
    @Mapping(target = "lastName", source = "name.lastName")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "phoneNumber", source = "phoneNumber.value")
    @Mapping(target = "zipCode", source = "address.zipCode")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "version", source = "version")
    fun toEntity(domain: Customer): CustomerEntity

    @Named("toCustomerName")
    fun toCustomerName(entity: CustomerEntity): CustomerName {
        return CustomerName(entity.firstName, entity.lastName)
    }

    @Named("toEmail")
    fun toEmail(email: String): Email {
        return Email(email)
    }

    @Named("toPhoneNumber")
    fun toPhoneNumber(phoneNumber: String): PhoneNumber {
        return PhoneNumber(phoneNumber)
    }

    @Named("toAddress")
    fun toAddress(entity: CustomerEntity): Address {
        return Address(entity.zipCode, entity.street, entity.city)
    }
}