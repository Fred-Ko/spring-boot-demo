package com.restaurant.customer.adapter.inbound.api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            e.message ?: "리소스를 찾을 수 없습니다."
        ).apply {
            title = "Resource Not Found"
            type = URI.create("https://api.restaurant.com/errors/not-found")
            setProperty("timestamp", Instant.now())
        }
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            e.message ?: "잘못된 요청입니다."
        ).apply {
            title = "Invalid Request"
            type = URI.create("https://api.restaurant.com/errors/bad-request")
            setProperty("timestamp", Instant.now())
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ProblemDetail {
        val errors = e.bindingResult.fieldErrors.associate { fieldError ->
            fieldError.field to (fieldError.defaultMessage ?: "올바르지 않은 값입니다.")
        }

        return ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "입력값 검증에 실패했습니다."
        ).apply {
            title = "Validation Failed"
            type = URI.create("https://api.restaurant.com/errors/validation-failed")
            setProperty("timestamp", Instant.now())
            setProperty("errors", errors)
        }
    }
}