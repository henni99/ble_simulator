package com.luxrobo.data.repositoryImpl

import com.luxrobo.data.datasourceImpl.BleTransferDataSource
import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BleTransferRepositoryImpl @Inject constructor(
    private val bleTransferDataSource: BleTransferDataSource
) : BleTransferRepository {
    override fun postMessage(message: Message): Flow<Message> {
        return flow {
            emit(bleTransferDataSource.postMessage(message))
        }
    }

    override fun getMessage(bleDeviceInfo: BleDeviceInfo): Flow<Message> {
        return flow {
            while(true) {
                emit(bleTransferDataSource.getMessage(bleDeviceInfo))
                delay(1000L)
            }
        }
    }


}