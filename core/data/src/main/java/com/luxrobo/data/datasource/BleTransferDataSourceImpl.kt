package com.luxrobo.data.datasource

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import com.luxrobo.virtual_api.MessageApi
import javax.inject.Inject

class BleTransferDataSourceImpl @Inject constructor(
    private val messageApi: MessageApi
) : BleTransferDataSource {
    override fun postMessage(message: Message) {
        return messageApi.postMessage(message)
    }

    override fun getMessage(bleDeviceInfo: BleDeviceInfo): Message {
        return messageApi.getMessage(bleDeviceInfo)
    }
}