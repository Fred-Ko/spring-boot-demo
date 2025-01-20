package com.restaurant.customer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.restaurant.customer", "com.ddd.outbox"])
@EnableJpaRepositories(basePackages = ["com.restaurant.customer", "com.ddd.outbox"])
@EntityScan(basePackages = ["com.restaurant.customer", "com.ddd.outbox"])
class CustomerApplication

fun main(args: Array<String>) {
    runApplication<CustomerApplication>(*args)
}