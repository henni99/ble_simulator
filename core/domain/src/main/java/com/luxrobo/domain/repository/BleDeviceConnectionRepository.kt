package com.luxrobo.domain.repository

import com.luxrobo.model.BleDeviceConnection
import kotlinx.coroutines.flow.Flow

interface BleDeviceConnectionRepository {

    fun getBleDeviceConnections(): Flow<List<BleDeviceConnection>>
}