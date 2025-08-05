package com.luxrobo.device_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.luxrobo.data_transfer.viewModel.BleDataTransferUiState
import com.luxrobo.device_list.presentation.BleDeviceListScreen
import com.luxrobo.device_list.viewModel.BleDeviceListUiState
import com.luxrobo.mock.mockBleDeviceConnections
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BleDeviceListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val fakeUiState: MutableState<BleDeviceListUiState> =
        mutableStateOf(BleDeviceListUiState.empty())

    @Before
    fun setup() {
        composeTestRule.setContent {
            BleDeviceListScreen(fakeUiState.value, {})
        }
    }

    @Test
    fun 블루투스를_통한_스캔_중일_상태() {
        fakeUiState.value = BleDeviceListUiState(
            isScanning = true,
            isDialogShowed = Pair(false, ""),
            bleDeviceConnections = mockBleDeviceConnections
        )

        composeTestRule
            .onNodeWithText("스캔 종료")
            .assertExists()

        composeTestRule
            .onNodeWithTag("스캔 애니메이션")
            .assertExists()

        composeTestRule
            .onNodeWithTag("카드")
            .assertExists()
    }


    @Test
    fun 블루투스를_통한_스캔_중이지_않은_상태() {
        fakeUiState.value = BleDeviceListUiState(
            isScanning = false,
            isDialogShowed = Pair(false, ""),
            bleDeviceConnections = emptyList()
        )

        composeTestRule
            .onNodeWithText("스캔 시작")
            .assertExists()

        composeTestRule
            .onNodeWithTag("스캔 애니메이션")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("카드")
            .assertDoesNotExist()
    }

    @Test
    fun 기기를_연결_중인_상태() {
        val newMockBleDeviceConnections = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65,
                isConnecting = true
            ),
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174001",
                name = "BLE_DEVICE_002",
                rssi = -70,
                isConnecting = false
            )
        )

        fakeUiState.value = BleDeviceListUiState(
            isScanning = true,
            isConnecting = false,
            isDialogShowed = Pair(false, ""),
            bleDeviceConnections = newMockBleDeviceConnections
        )

        composeTestRule
            .onNodeWithTag("로딩")
            .assertDoesNotExist()

        composeTestRule
            .onAllNodesWithTag("연결 버튼")
            .onFirst()
            .assertIsNotEnabled()
    }

    @Test
    fun 기기를_연결_중이지_않은_상태() {
        val newMockBleDeviceConnections = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65,
                isConnecting = false
            ),
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174001",
                name = "BLE_DEVICE_002",
                rssi = -70,
                isConnecting = false
            )
        )

        fakeUiState.value = BleDeviceListUiState(
            isScanning = true,
            isConnecting = false,
            isDialogShowed = Pair(false, ""),
            bleDeviceConnections = newMockBleDeviceConnections
        )

        composeTestRule
            .onNodeWithTag("로딩")
            .assertDoesNotExist()

        composeTestRule
            .onAllNodesWithTag("연결 버튼")
            .onFirst()
            .assertIsEnabled()

        composeTestRule
            .onAllNodesWithTag("연결 버튼")
            .onLast()
            .assertIsEnabled()
    }

    @Test
    fun RSSI_로인해_Dialog를_보여주는_경우() {
        val newMockBleDeviceConnections = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65,
                isConnecting = false
            ),
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174001",
                name = "BLE_DEVICE_002",
                rssi = -70,
                isConnecting = true
            )
        )

        fakeUiState.value = BleDeviceListUiState(
            isScanning = true,
            isConnecting = true,
            isDialogShowed = Pair(true, ""),
            bleDeviceConnections = newMockBleDeviceConnections
        )

        composeTestRule
            .onNodeWithTag("Dialog")
            .assertExists()
    }

    @Test
    fun Dialog를_보여주지_않는_경우() {
        val newMockBleDeviceConnections = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65,
                isConnecting = true
            ),
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174001",
                name = "BLE_DEVICE_002",
                rssi = -70,
                isConnecting = false
            )
        )

        fakeUiState.value = BleDeviceListUiState(
            isScanning = true,
            isConnecting = true,
            isDialogShowed = Pair(false, ""),
            bleDeviceConnections = newMockBleDeviceConnections
        )

        composeTestRule
            .onNodeWithTag("Dialog")
            .assertDoesNotExist()
    }
}