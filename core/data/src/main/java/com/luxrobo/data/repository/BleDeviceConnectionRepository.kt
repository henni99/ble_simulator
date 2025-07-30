package com.luxrobo.data.repository

import com.luxrobo.model.BleDeviceConnection
import kotlinx.coroutines.flow.Flow

interface BleDeviceConnectionRepository {

    suspend fun getBleDeviceConnections(): Flow<List<BleDeviceConnection>>
}