package com.luxrobo.domain.usecase

import com.luxrobo.domain.repository.BleDeviceConnectionRepository
import com.luxrobo.model.BleDeviceConnection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * GetBleDeviceConnectionsUseCase
 *
 * BLE 디바이스 연결 목록을 가져오는 UseCase 클래스.
 * 내부에서 BleDeviceConnectionRepository의 getBleDeviceConnections 메서드를 호출하여
 * 연결된 BLE 디바이스 리스트를 Flow 형태로 반환합니다.
 */

@Singleton
class GetBleDeviceConnectionsUseCase @Inject constructor(
    private val bleRepository: BleDeviceConnectionRepository
) {
    operator fun invoke(): Flow<List<BleDeviceConnection>> {
        return bleRepository.getBleDeviceConnections()
    }
}