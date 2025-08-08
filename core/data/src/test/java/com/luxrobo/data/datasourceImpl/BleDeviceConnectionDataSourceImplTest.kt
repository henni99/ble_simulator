package com.luxrobo.data.datasourceImpl

import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.virtual_api.DeviceApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*


class BleDeviceConnectionDataSourceImplTest {

    private lateinit var deviceApi: DeviceApi
    private lateinit var dataSource: BleDeviceConnectionDataSourceImpl

    @BeforeEach
    fun setUp() {
        deviceApi = mock()
        dataSource = BleDeviceConnectionDataSourceImpl(deviceApi)
    }

    @Test
    fun `deviceApi를_통해_값을_받아오고 결과를 반환한다`() = runTest {
        // Given
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
        whenever(deviceApi.getDeviceConnections()).thenReturn(expectedConnections)

        // When
        val result = dataSource.getBleDeviceConnections()

        // Then:
        assertEquals(expectedConnections, result)
        verify(deviceApi).getDeviceConnections()
    }
}