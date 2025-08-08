package com.luxrobo.data.repositoryImpl

import app.cash.turbine.test
import com.luxrobo.data.datasourceImpl.BleTransferDataSource
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class BleTransferRepositoryImplTest {

    private lateinit var dataSource: BleTransferDataSource
    private lateinit var repository: BleTransferRepositoryImpl

    @BeforeEach
    fun setUp() {
        dataSource = mock()
        repository = BleTransferRepositoryImpl(dataSource)
    }

    @Test
    fun `postMessage를 통해 단일 메시지 가져오고 flow에 emit 한다`() = runTest {
        // given
        val message =  Message(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            message = "test message"
        )
        whenever(dataSource.postMessage(message)).thenReturn(message)

        // when & then
        repository.postMessage(message).test {
            val emitted = awaitItem()
            assertEquals(message, emitted)
            awaitComplete()
        }

        verify(dataSource).postMessage(message)
    }

    @Test
    fun `getMessage는 가상의 메시지를 주기적(1초)으로 받아와 emit 한다`() = runTest {
        // given
        val bleDeviceInfo = BleDeviceInfo(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val message =  Message(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            message = "test message"
        )

        whenever(dataSource.getMessage(bleDeviceInfo)).thenReturn(message)

        // when & then
        repository.getMessage(bleDeviceInfo).test {
            val emitted1 = awaitItem()
            assertEquals(message, emitted1)

            advanceTimeBy(1000L)
            val emitted2 = awaitItem()
            assertEquals(message, emitted2)

            cancelAndIgnoreRemainingEvents()
        }

        verify(dataSource, atLeast(2)).getMessage(bleDeviceInfo)
    }
}