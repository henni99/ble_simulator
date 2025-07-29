package com.luxrobo.mapper

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val currentTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)