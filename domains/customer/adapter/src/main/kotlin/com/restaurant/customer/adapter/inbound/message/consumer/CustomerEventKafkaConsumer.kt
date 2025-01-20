package com.restaurant.customer.adapter.inbound.message.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.restaurant.customer.adapter.inbound.message.handler.CustomerEventHandler
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CustomerEventKafkaConsumer(
    private val objectMapper: ObjectMapper,
    private val customerEventHandler: CustomerEventHandler
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        topics = ["Customer"],
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun consume(@Payload message: String) {
        log.info("Received message: {}", message)
        try {
            customerEventHandler.handle(message)
        } catch (e: Exception) {
            log.error("Failed to process message: {}", message, e)
        }
    }
} 