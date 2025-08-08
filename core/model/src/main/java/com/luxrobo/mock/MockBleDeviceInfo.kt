package com.luxrobo.mock

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo

/**
 * mockBleDeviceConnections
 *
 * BLE 디바이스 연결 정보의 샘플(모의) 데이터 리스트.
 * 테스트 및 개발용으로 실제 연결된 장치 대신 사용됨.
 */

val mockBleDeviceConnections = listOf(
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174000",
        name = "BLE_DEVICE_001",
        rssi = -65
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174001",
        name = "BLE_DEVICE_002",
        rssi = -70
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174002",
        name = "BLE_DEVICE_003",
        rssi = -80
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174003",
        name = "BLE_DEVICE_004",
        rssi = -75
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174004",
        name = "BLE_DEVICE_005",
        rssi = -60
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174005",
        name = "BLE_DEVICE_006",
        rssi = -68
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174006",
        name = "BLE_DEVICE_007",
        rssi = -82
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174007",
        name = "BLE_DEVICE_008",
        rssi = -77
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174008",
        name = "BLE_DEVICE_009",
        rssi = -69
    ),
    BleDeviceConnection(
        deviceId = "123e4567-e89b-12d3-a456-426614174009",
        name = "BLE_DEVICE_010",
        rssi = -73
    )
)
