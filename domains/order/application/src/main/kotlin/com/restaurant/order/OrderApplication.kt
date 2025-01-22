package com.restaurant.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["com.restaurant.order", "com.ddd.outbox"])
@EnableJpaRepositories(basePackages = ["com.restaurant.order", "com.ddd.outbox"])
@EntityScan(basePackages = ["com.restaurant.order", "com.ddd.outbox"])
@EnableScheduling
class OrderApplication

fun main(args: Array<String>) {
  runApplication<OrderApplication>(*args)
}
