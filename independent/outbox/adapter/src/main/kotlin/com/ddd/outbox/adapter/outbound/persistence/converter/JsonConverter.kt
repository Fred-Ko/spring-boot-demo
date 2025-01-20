package com.ddd.outbox.adapter.outbound.persistence.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class JsonConverter : AttributeConverter<Any, String> {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    override fun convertToDatabaseColumn(attribute: Any?): String {
        if (attribute == null) {
            throw IllegalArgumentException("Payload cannot be null")
        }
        return try {
            objectMapper.writeValueAsString(attribute)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to convert payload to JSON", e)
        }
    }

    override fun convertToEntityAttribute(dbData: String?): Any {
        if (dbData == null) {
            throw IllegalArgumentException("Stored payload cannot be null")
        }
        return try {
            objectMapper.readValue(dbData, Any::class.java)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to convert JSON to payload", e)
        }
    }
}