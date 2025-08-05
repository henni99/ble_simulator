package com.luxrobo.data.datasourceImpl

import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message

interface BleTransferDataSource {

    fun postMessage(message: Message): Message

    fun getMessage(bleDeviceInfo: BleDeviceInfo): Message
}