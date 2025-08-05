package com.luxrobo.domain

import app.cash.turbine.test
import com.luxrobo.domain.repository.BleDeviceConnectionRepository
import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.domain.usecase.GetBleDeviceConnectionsUseCase
import com.luxrobo.domain.usecase.GetMessageUseCase
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetMessageUseCaseTest {


    lateinit var bleTransferRepository: BleTransferRepository
    lateinit var getMessageUseCase: GetMessageUseCase

    @BeforeEach
    fun setUp() {

        bleTransferRepository = mockk()
        getMessageUseCase = GetMessageUseCase(bleTransferRepository)
    }

    @Test
    fun `invoke should return message from repository`() = runTest {
        val bleDeviceInfo = BleDeviceInfo(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )

        val message = Message(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            message = "test message"
        )

        every { bleTransferRepository.getMessage(bleDeviceInfo) } returns flowOf(message)

        // when
        val resultFlow = getMessageUseCase(bleDeviceInfo)

        // then
        resultFlow.test {
            val result = awaitItem()
            assertEquals(message, result)
            awaitComplete()
        }
    }
}