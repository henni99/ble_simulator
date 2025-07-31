package com.luxrobo.domain.usecase

import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostMessageUseCase @Inject constructor(
    private val bleTransferRepository: BleTransferRepository
) {
    operator fun invoke(message: Message): Flow<Message> {
        return bleTransferRepository.postMessage(message)

    }
}