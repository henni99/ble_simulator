package com.luxrobo.domain

import app.cash.turbine.test
import com.luxrobo.domain.repository.BleDeviceConnectionRepository
import com.luxrobo.domain.repository.BleTransferRepository
import com.luxrobo.domain.usecase.GetBleDeviceConnectionsUseCase
import com.luxrobo.domain.usecase.GetMessageUseCase
import com.luxrobo.domain.usecase.PostMessageUseCase
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

class PostMessageUseCaseTest {


    lateinit var bleTransferRepository: BleTransferRepository
    lateinit var postMessageUseCase: PostMessageUseCase

    @BeforeEach
    fun setUp() {

        bleTransferRepository = mockk()
        postMessageUseCase = PostMessageUseCase(bleTransferRepository)
    }

    @Test
    fun `invoke should return message from repository`() = runTest {
        val message = Message(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            message = "test message"
        )

        every { bleTransferRepository.postMessage(message) } returns flowOf(message)

        // when
        val resultFlow = postMessageUseCase(message)

        // then
        resultFlow.test {
            val result = awaitItem()
            assertEquals(message, result)
            awaitComplete()
        }
    }
}