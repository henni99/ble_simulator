package com.luxrobo.model

import kotlinx.serialization.Serializable

@Serializable
data class BleDeviceInfo(
    val deviceId: String,
    val name: String,
    val rssi: Long
)

