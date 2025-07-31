package com.luxrobo.virtual_api

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import javax.inject.Inject
import kotlin.random.Random


class MessageApi @Inject constructor() {

    fun postMessage(message: Message) { }

    fun getMessage(bleDeviceInfo: BleDeviceInfo): Message {
        return Message(
            deviceId = bleDeviceInfo.deviceId,
            name = bleDeviceInfo.name,
            message = bleDeviceInfo.deviceId + " " + bleDeviceInfo.name + " " + getConnectionQuality(bleDeviceInfo.rssi)
             + " " + generateRandomString(10)
        )
    }
}

internal fun getConnectionQuality(rssi: Long): String {
    return when {
        rssi > -60 -> "매우 안정적인 연결"
        rssi in -70 until -60 -> "안정적인 연결"
        rssi in -80 until -70 -> "연결 가능하지만 불안정, 끊김 가능성 있음"
        rssi <= -80 -> "매우 불안정, 연결 실패 또는 자주 끊김"
        else -> "알 수 없음"
    }
}

internal fun generateRandomString(length: Int): String {
    val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}