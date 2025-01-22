package com.restaurant.order.adapter.inbound.message.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.restaurant.order.adapter.inbound.message.handler.MenuEventHandler
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Component

@Component
class MenuEventKafkaConsumer(
        private val objectMapper: ObjectMapper,
        private val menuEventHandler: MenuEventHandler
) {
  private val log = LoggerFactory.getLogger(this::class.java)

  @RetryableTopic(attempts = "3", backoff = Backoff(delay = 1000, multiplier = 2.0))
  @KafkaListener(topics = ["com.restaurant.order"], groupId = "\${spring.application.name}")
  fun consume(@Payload message: String) {
    try {
      log.debug("Consuming message: {}", message)
      menuEventHandler.handle(message)
    } catch (e: Exception) {
      log.error("Error processing message: {}", message, e)
      throw e
    }
  }
}
