package com.luxrobo.data.repositoryImpl

import com.luxrobo.data.datasourceImpl.BleDeviceConnectionDataSource
import com.luxrobo.domain.repository.BleDeviceConnectionRepository
import com.luxrobo.model.BleDeviceConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BleDeviceConnectionRepositoryImpl @Inject constructor(
    private val bleDeviceConnectionDataSource: BleDeviceConnectionDataSource
): BleDeviceConnectionRepository {
    override fun getBleDeviceConnections(): Flow<List<BleDeviceConnection>> {
        return flow {
            emit(bleDeviceConnectionDataSource.getBleDeviceConnections())
        }
    }
}