package com.luxrobo.data.datasource

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
    fun `postMessage는 messageApi에 위임하고 결과를 반환한다`() = runTest {
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
    fun `postMessage에서 messageApi가 예외를 던지면 그대로 예외를 던진다`() = runTest {
        // given
        val message = Message(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            message = "test message"
        )
        val expectedErrorMessage = "messageApi failed"
        whenever(messageApi.postMessage(message)).thenThrow(RuntimeException(expectedErrorMessage))

        // when
        val exception = assertThrows(RuntimeException::class.java)  {
            dataSource.postMessage(message)
        }

        // then
        assertEquals(expectedErrorMessage, exception.message)
        verify(messageApi).postMessage(message)
    }

    @Test
    fun `getMessage는 messageApi에 위임하고 결과를 반환한다`() = runTest {
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

    @Test
    fun `getMessage에서 messageApi가 예외를 던지면 그대로 예외를 던진다`() = runTest {
        // given
        val bleDeviceInfo = BleDeviceInfo(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedErrorMessage = "messageApi failed"
        whenever(messageApi.getMessage(bleDeviceInfo)).thenThrow(RuntimeException(expectedErrorMessage))

        // when
        val exception = assertThrows(RuntimeException::class.java) {
            dataSource.getMessage(bleDeviceInfo)
        }

        // then
        assertEquals(expectedErrorMessage, exception.message)
        verify(messageApi).getMessage(bleDeviceInfo)
    }
}