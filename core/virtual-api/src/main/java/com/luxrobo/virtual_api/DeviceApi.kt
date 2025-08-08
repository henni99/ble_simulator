package com.luxrobo.virtual_api

import com.luxrobo.mock.mockBleDeviceConnections
import com.luxrobo.model.BleDeviceConnection
import javax.inject.Inject

/**
 * DeviceApi
 *
 * BLE 디바이스 연결 정보를 관리하는 API 역할의 클래스.
 * 현재는 모의(mock) 데이터를 반환함.
 */

class DeviceApi @Inject constructor() {

    /**
     * getDeviceConnections
     *
     * 연결된 BLE 디바이스 목록을 반환.
     * 실제 데이터 대신 현재는 모의 데이터(mockBleDeviceConnections)를 반환함.
     *
     * @return BLE 디바이스 연결 정보 리스트
     */

    fun getDeviceConnections(): List<BleDeviceConnection> {
        return mockBleDeviceConnections
    }
}