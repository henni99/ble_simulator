package com.luxrobo.data.repository

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.Message
import kotlinx.coroutines.flow.Flow

interface BleTransferRepository {

    fun postMessage(message: Message): Flow<Unit>

    fun getMessage(bleDeviceConnection: BleDeviceConnection): Flow<Message>
}