package com.luxrobo.virtual_api

import com.luxrobo.ble_simulator.core.virtual.api.R
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import javax.inject.Inject

/**
 * MessageApi
 *
 * 메시지 관련 API 역할을 하는 클래스.
 * - 메시지 전송(postMessage)
 * - BLE 장치 정보를 받아 메시지 생성(getMessage)
 */

class MessageApi @Inject constructor() {

    /**
     * postMessage
     *
     * 메시지를 서버 등에 전송하는 기능 (현재는 입력 메시지를 그대로 반환).
     *
     * @param message 전송할 메시지 객체
     * @return 전송된 메시지 객체
     */

    fun postMessage(message: Message): Message {
        return message
    }

    /**
     * getMessage
     *
     * BLE 장치 정보를 바탕으로 메시지 객체를 생성하여 반환.
     * 메시지 문자열에는 deviceId, name, RSSI 상태, 랜덤 문자열 포함.
     *
     * @param bleDeviceInfo BLE 장치 정보
     * @return 생성된 메시지 객체
     */

    fun getMessage(bleDeviceInfo: BleDeviceInfo): Message {
        return Message(
            deviceId = bleDeviceInfo.deviceId,
            name = bleDeviceInfo.name,
            message = "deviceId: " + bleDeviceInfo.deviceId + "\n" +
                    "name: " + bleDeviceInfo.name + "\n" +
                    "rssi: " + getConnectionQuality(bleDeviceInfo.rssi) + "\n" +
                    "random: " + generateRandomString(10) + "\n"
        )
    }
}

/**
 * getConnectionQuality
 *
 * RSSI 값을 받아 연결 품질 상태 문자열 리소스 ID를 문자열로 변환하여 반환 (현재는 단순 toString 호출).
 *
 * @param rssi RSSI 신호 세기 값
 * @return 연결 품질 상태 문자열 (리소스 ID의 문자열 표현)
 */

internal fun getConnectionQuality(rssi: Long): String {
    return when {
        rssi > -60 -> R.string.connection_quality_excellent
        rssi in -70 until -60 -> R.string.connection_quality_good
        rssi in -80 until -70 -> R.string.connection_quality_fair
        rssi <= -80 -> R.string.connection_quality_poor
        else -> R.string.connection_quality_unknown
    }.toString()
}

/**
 * generateRandomString
 *
 * 지정한 길이만큼 영문 대소문자와 숫자를 랜덤으로 조합한 문자열 생성.
 *
 * @param length 생성할 문자열 길이
 * @return 랜덤 문자열
 */

internal fun generateRandomString(length: Int): String {
    val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}