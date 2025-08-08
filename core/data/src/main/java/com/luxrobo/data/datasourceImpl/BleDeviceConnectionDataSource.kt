package com.luxrobo.data.datasourceImpl

import com.luxrobo.model.BleDeviceConnection

/**
 * BleDeviceConnectionDataSource
 *
 * BLE 디바이스 연결 정보를 제공하는 데이터 소스 인터페이스.
 * 구현체는 BLE 디바이스 연결 목록을 반환하는 기능을 구현해야 합니다.
 */

interface BleDeviceConnectionDataSource {

    /**
     * getBleDeviceConnections
     *
     * 현재 연결된 BLE 디바이스들의 리스트를 반환합니다.
     *
     * @return BLE 디바이스 연결 정보 리스트
     */

    fun getBleDeviceConnections(): List<BleDeviceConnection>
}