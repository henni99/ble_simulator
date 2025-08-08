package com.luxrobo.mapper

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo

/**
 * BleDeviceConnection.toBleDeviceInfo
 *
 * 확장 함수로서, BleDeviceConnection 객체를
 * BleDeviceInfo 객체로 변환하여 반환합니다.
 *
 * @receiver 변환할 BleDeviceConnection 객체
 * @return 변환된 BleDeviceInfo 객체
 */

fun BleDeviceConnection.toBleDeviceInfo() = BleDeviceInfo(
    deviceId = deviceId,
    name = name,
    rssi = rssi
)