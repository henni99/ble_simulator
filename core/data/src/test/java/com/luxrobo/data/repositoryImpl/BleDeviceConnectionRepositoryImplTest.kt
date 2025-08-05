package com.luxrobo.data.repositoryImpl

import app.cash.turbine.test
import com.luxrobo.data.datasourceImpl.BleDeviceConnectionDataSource
import com.luxrobo.model.BleDeviceConnection
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class BleDeviceConnectionRepositoryImplTest {

    private lateinit var dataSource: BleDeviceConnectionDataSource
    private lateinit var repository: BleDeviceConnectionRepositoryImpl

    @BeforeEach
    fun setUp() {
        dataSource = mock()
        repository = BleDeviceConnectionRepositoryImpl(dataSource)
    }

    @Test
    fun `BLE 디바이스 연결 목록을 정상적으로 가져온다`() = runTest {
        // given
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
        whenever(dataSource.getBleDeviceConnections()).thenReturn(expectedConnections)

        // when & then
        repository.getBleDeviceConnections().test {
            val result = awaitItem()
            assert(result == expectedConnections)
            awaitComplete()
        }

        verify(dataSource).getBleDeviceConnections()
    }

    @Test
    fun `BLE 디바이스 연결 목록 조회 시 예외가 발생하면 흐름이 실패한다`() = runTest {
        // given
        val expectedMessage = "데이터 소스 실패"
        whenever(dataSource.getBleDeviceConnections()).thenThrow(RuntimeException(expectedMessage))

        // when & then
        repository.getBleDeviceConnections().test {
            val exception = awaitError()
            assert(exception is RuntimeException)
            assertEquals(expectedMessage, exception.message)
        }

        verify(dataSource).getBleDeviceConnections()
    }
}