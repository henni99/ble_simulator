package com.luxrobo.data.datasource

import com.luxrobo.model.BleDeviceConnection

interface BleDeviceConnectionDataSource {

    suspend fun getBleDeviceConnections(): List<BleDeviceConnection>
}