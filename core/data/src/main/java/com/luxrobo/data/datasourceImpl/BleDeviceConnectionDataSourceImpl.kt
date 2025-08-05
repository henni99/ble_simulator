package com.luxrobo.data.datasourceImpl

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.virtual_api.DeviceApi
import javax.inject.Inject

class BleDeviceConnectionDataSourceImpl @Inject constructor(
    private val deviceApi: DeviceApi
): BleDeviceConnectionDataSource {
    override fun getBleDeviceConnections(): List<BleDeviceConnection> {
        return deviceApi.getDeviceConnections()
    }
}