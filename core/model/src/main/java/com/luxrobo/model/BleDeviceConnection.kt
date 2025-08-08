package com.luxrobo.model

import com.luxrobo.mapper.currentTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * BleDeviceConnection
 *
 * BLE 기기의 연결 정보를 나타내는 데이터 클래스.
 *
 * @property datetime       데이터 생성 또는 기록 시점 (기본값: 현재 시간)
 * @property deviceId       BLE 기기의 고유 식별자
 * @property name           BLE 기기 이름
 * @property rssi           신호 세기(RSSI, Received Signal Strength Indicator)
 * @property isConnecting   현재 연결 중인지 여부 (기본값: false)
 */

@Serializable
data class BleDeviceConnection(
    val datetime: LocalDateTime = currentTime,
    val deviceId: String,
    val name: String,
    val rssi: Long,
    val isConnecting: Boolean = false
)

