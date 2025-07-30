package com.luxrobo.domain.usecase

import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMessageUseCase @Inject constructor(
    private val bleTransferRepository: BleTransferRepository
){
    operator fun invoke(bleDeviceConnection: BleDeviceConnection): Flow<Message> {
        return bleTransferRepository.getMessage(bleDeviceConnection)
    }

}