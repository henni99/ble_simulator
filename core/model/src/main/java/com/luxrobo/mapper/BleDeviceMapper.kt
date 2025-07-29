package com.luxrobo.mapper

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo

fun BleDeviceConnection.toBleDeviceInfo() = BleDeviceInfo(
    deviceId = deviceId,
    name = name
)