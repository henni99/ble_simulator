package com.luxrobo.mapper

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 현재 시스템 시간을 UTC 기준으로
 * LocalDateTime 형태로 가져옵니다.
 */

val currentTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)