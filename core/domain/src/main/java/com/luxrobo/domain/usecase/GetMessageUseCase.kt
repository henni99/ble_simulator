package com.luxrobo.domain.usecase

import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * GetMessageUseCase
 *
 * BLE 기기 정보를 받아서 메시지를 비동기 스트림(Flow)으로 제공하는 UseCase 클래스.
 * 내부에서 BleTransferRepository의 getMessage 메서드를 호출하여 데이터 획득.
 */

@Singleton
class GetMessageUseCase @Inject constructor(
    private val bleTransferRepository: BleTransferRepository
) {
    operator fun invoke(bleDeviceInfo: BleDeviceInfo): Flow<Message> {
        return bleTransferRepository.getMessage(bleDeviceInfo)
    }
}