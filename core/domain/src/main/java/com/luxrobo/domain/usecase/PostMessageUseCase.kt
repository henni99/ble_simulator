package com.luxrobo.domain.usecase

import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * PostMessageUseCase
 *
 * 메시지를 전송하는 UseCase 클래스.
 * BleTransferRepository의 postMessage 메서드를 호출하여 메시지 전송을 수행하고,
 * 결과를 Flow 형태로 반환합니다.
 */

@Singleton
class PostMessageUseCase @Inject constructor(
    private val bleTransferRepository: BleTransferRepository
) {
    operator fun invoke(message: Message): Flow<Message> {
        return bleTransferRepository.postMessage(message)
    }
}