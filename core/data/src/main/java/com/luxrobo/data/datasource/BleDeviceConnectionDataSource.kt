package com.luxrobo.data.datasource

import com.luxrobo.model.BleDeviceConnection

interface BleDeviceConnectionDataSource {

    fun getBleDeviceConnections(): List<BleDeviceConnection>
}