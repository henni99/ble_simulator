package com.luxrobo.data.datasource

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
    fun `getBleDeviceConnections는 deviceApi에 위임하고 결과를 반환한다`() = runTest {
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
        whenever(deviceApi.getDeviceConnections()).thenReturn(expectedConnections)

        // when
        val result = dataSource.getBleDeviceConnections()

        // then
        assertEquals(expectedConnections, result)
        verify(deviceApi).getDeviceConnections()
    }

    @Test
    fun `getBleDeviceConnections는 deviceApi가 실패하면 예외를 던집니다`() = runTest {

        // given
        val expectedErrorMessage = "deviceApi failed as expected"
        whenever(deviceApi.getDeviceConnections()).thenThrow(RuntimeException(expectedErrorMessage))

        // when & then
        val exception = assertThrows(RuntimeException::class.java) { // <-- 예외 클래스::class.java 명시
            dataSource.getBleDeviceConnections() // 일반 함수 호출
        }
        assertEquals(expectedErrorMessage, exception.message)

        // verify
        verify(deviceApi).getDeviceConnections()
    }
}