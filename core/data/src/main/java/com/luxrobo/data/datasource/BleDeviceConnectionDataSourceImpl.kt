package com.luxrobo.data.datasource

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.virtual_api.DeviceApi
import javax.inject.Inject

class BleDeviceConnectionDataSourceImpl @Inject constructor(
    private val deviceApi: DeviceApi
): BleDeviceConnectionDataSource {
    override suspend fun getBleDeviceConnections(): List<BleDeviceConnection> {
        return deviceApi.getDeviceConnections()
    }
}