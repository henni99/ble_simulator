package com.luxrobo.domain

import app.cash.turbine.test
import com.luxrobo.domain.repository.BleDeviceConnectionRepository
import com.luxrobo.domain.usecase.GetBleDeviceConnectionsUseCase
import com.luxrobo.mock.mockBleDeviceConnections
import com.luxrobo.model.BleDeviceConnection
import kotlinx.coroutines.flow.Flow
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.test.assertEquals

class GetBleDeviceConnectionsUseCaseTest  {

    lateinit var connectionRepository: BleDeviceConnectionRepository
    lateinit var getBleDeviceConnectionsUseCase: GetBleDeviceConnectionsUseCase

    @BeforeEach
    fun setUp() {
        connectionRepository = mockk()
        getBleDeviceConnectionsUseCase = GetBleDeviceConnectionsUseCase(connectionRepository)
    }

    @Test
    fun `BLE 디바이스 연결 목록을 정상적으로 가져온다`() = runTest {
        coEvery { connectionRepository.getBleDeviceConnections() } returns flowOf(mockBleDeviceConnections)

        val expectedConnections = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65
            ),
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174001",
                name = "BLE_DEVICE_002",
                rssi = -70
            )
        )


        every { connectionRepository.getBleDeviceConnections() } returns flowOf(expectedConnections)

        // when
        val resultFlow = getBleDeviceConnectionsUseCase.invoke()

        // then
        resultFlow.test {
            val result = awaitItem()
            assertEquals(expectedConnections, result)
            awaitComplete()
        }
    }
}