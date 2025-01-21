package com.restaurant.order.core.domain.model.vo

import java.time.LocalDateTime
import java.time.LocalTime

data class OperatingHours(val startTime: LocalTime, val endTime: LocalTime) {
  init {
    require(startTime.isBefore(endTime)) { "시작 시간은 종료 시간보다 이전이어야 합니다." }
  }

  fun isWithinOperatingHours(dateTime: LocalDateTime): Boolean {
    val time = dateTime.toLocalTime()
    return !time.isBefore(startTime) && !time.isAfter(endTime)
  }
}
