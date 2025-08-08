package com.luxrobo.data.datasourceImpl

import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import com.luxrobo.virtual_api.MessageApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class BleTransferDataSourceImplTest {

    private lateinit var messageApi: MessageApi
    private lateinit var dataSource: BleTransferDataSourceImpl

    @BeforeEach
    fun setUp() {
        messageApi = mock()
        dataSource = BleTransferDataSourceImpl(messageApi)
    }

    @Test
    fun `messageApi를 사용하면_전달된_상황을_가정하고_그대로 반환한다`() = runTest {
        // given
        val message = Message(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            message = "test message"
        )
        whenever(messageApi.postMessage(message)).thenReturn(message)

        // when
        val result = dataSource.postMessage(message)

        // then
        assertEquals(message, result)
        verify(messageApi).postMessage(message)
    }

    @Test
    fun `messageApi를 통해_가상의_결과를 반환한다`() = runTest {
        // given
        val bleDeviceInfo = BleDeviceInfo(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedMessage = Message(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            message = "test message"
        )
        whenever(messageApi.getMessage(bleDeviceInfo)).thenReturn(expectedMessage)

        // when
        val result = dataSource.getMessage(bleDeviceInfo)

        // then
        assertEquals(expectedMessage, result)
        verify(messageApi).getMessage(bleDeviceInfo)
    }

}