package com.luxrobo.model

import kotlinx.serialization.Serializable

/**
 * BleDeviceInfo
 *
 * BLE 기기의 기본 정보를 담는 데이터 클래스.
 *
 * @property deviceId BLE 기기의 고유 식별자
 * @property name     BLE 기기 이름
 * @property rssi     신호 세기(RSSI, Received Signal Strength Indicator)
 */

@Serializable
data class BleDeviceInfo(
    val deviceId: String,
    val name: String,
    val rssi: Long
)

