package com.luxrobo.data.datasource

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message

interface BleTransferDataSource {

    fun postMessage(message: Message): Unit

    fun getMessage(bleDeviceInfo: BleDeviceInfo): Message
}