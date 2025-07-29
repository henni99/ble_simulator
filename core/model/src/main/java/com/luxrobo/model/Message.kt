package com.luxrobo.model

import com.luxrobo.mapper.currentTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val datetime: LocalDateTime = currentTime,
    val deviceId: String,
    val name: String,
    val message: String
)


