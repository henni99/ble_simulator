package com.luxrobo.data.repository

import com.luxrobo.data.datasource.BleDeviceConnectionDataSource
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.virtual_api.DeviceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BleDeviceConnectionRepositoryImpl @Inject constructor(
    private val bleDeviceConnectionDataSource: BleDeviceConnectionDataSource
): BleDeviceConnectionRepository {
    override suspend fun getBleDeviceConnections(): Flow<List<BleDeviceConnection>> {
        return flow {
            emit(bleDeviceConnectionDataSource.getBleDeviceConnections())
        }
    }
}