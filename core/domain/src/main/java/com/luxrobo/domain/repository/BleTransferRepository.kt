package com.luxrobo.domain.repository

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import kotlinx.coroutines.flow.Flow

interface BleTransferRepository {

    fun postMessage(message: Message): Flow<Unit>

    fun getMessage(bleDeviceInfo: BleDeviceInfo): Flow<Message>
}