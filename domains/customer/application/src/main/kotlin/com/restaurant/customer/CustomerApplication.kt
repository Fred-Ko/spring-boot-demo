package com.restaurant.customer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["com.restaurant.customer", "com.ddd.outbox"])
@EnableJpaRepositories(basePackages = ["com.restaurant.customer", "com.ddd.outbox"])
@EntityScan(basePackages = ["com.restaurant.customer", "com.ddd.outbox"])
@EnableScheduling
class CustomerApplication

fun main(args: Array<String>) {
    runApplication<CustomerApplication>(*args)
}