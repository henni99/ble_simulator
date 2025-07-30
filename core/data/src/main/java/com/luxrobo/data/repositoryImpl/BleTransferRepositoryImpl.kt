package com.luxrobo.data.repositoryImpl

import com.luxrobo.data.datasource.BleTransferDataSource
import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BleTransferRepositoryImpl @Inject constructor(
    private val bleTransferDataSource: BleTransferDataSource
) : BleTransferRepository {
    override fun postMessage(message: Message): Flow<Unit> {
        return flow {
            emit(bleTransferDataSource.postMessage(message))
        }
    }

    override fun getMessage(bleDeviceConnection: BleDeviceConnection): Flow<Message> {
        return flow {
            emit(bleTransferDataSource.getMessage(bleDeviceConnection))
        }
    }


}