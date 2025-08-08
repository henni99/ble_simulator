package com.luxrobo.data.datasourceImpl

import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message

/**
 * BleTransferDataSource
 *
 * BLE 메시지 전송 및 수신을 위한 데이터 소스 인터페이스.
 * 구현체는 메시지 전송과 BLE 기기 정보 기반 메시지 수신 기능을 제공해야 합니다.
 */

interface BleTransferDataSource {

    /**
     * postMessage
     *
     * 메시지를 전송하는 기능.
     *
     * @param message 전송할 메시지 객체
     * @return 전송 결과 메시지 객체
     */

    fun postMessage(message: Message): Message

    /**
     * getMessage
     *
     * BLE 기기 정보를 바탕으로 메시지를 수신하거나 생성하는 기능.
     *
     * @param bleDeviceInfo BLE 기기 정보
     * @return 수신 또는 생성된 메시지 객체
     */

    fun getMessage(bleDeviceInfo: BleDeviceInfo): Message
}