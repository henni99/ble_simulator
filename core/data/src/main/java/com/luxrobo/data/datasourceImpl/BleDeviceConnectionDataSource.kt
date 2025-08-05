package com.luxrobo.data.datasourceImpl

import com.luxrobo.model.BleDeviceConnection

interface BleDeviceConnectionDataSource {

    fun getBleDeviceConnections(): List<BleDeviceConnection>
}