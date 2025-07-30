package com.luxrobo.virtual_api

import com.luxrobo.mock.mockBleDeviceConnections
import com.luxrobo.model.BleDeviceConnection
import javax.inject.Inject


class DeviceApi @Inject constructor() {
    fun getDeviceConnections(): List<BleDeviceConnection> {
        return mockBleDeviceConnections
    }
}