package com.luxrobo.domain.usecase

import com.luxrobo.domain.repository.BleDeviceConnectionRepository
import com.luxrobo.model.BleDeviceConnection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBleDeviceConnectionsUseCase @Inject constructor(
    private val bleRepository: BleDeviceConnectionRepository
) {
    operator fun invoke(): Flow<List<BleDeviceConnection>> {
        return bleRepository.getBleDeviceConnections()
    }
}