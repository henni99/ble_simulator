package com.luxrobo.data_transfer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.luxrobo.data_transfer.presentation.BleDataTransferScreen
import com.luxrobo.data_transfer.viewModel.BleDataTransferUiState
import com.luxrobo.model.BleDeviceInfo
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class BleDataTransferScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val fakeUiState: MutableState<BleDataTransferUiState> =
        mutableStateOf(BleDataTransferUiState.empty())

    @Before
    fun setup() {
        composeTestRule.setContent {
            BleDataTransferScreen(fakeUiState.value, {})
        }
    }

    @Test
    fun `BleDataTransferScreen_초기상태_올바르게표시됨`() {
        fakeUiState.value = BleDataTransferUiState(
            deviceInfo = BleDeviceInfo(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65,
            ),
            sendMessages = "test_send_message1\ntest_send_message2\ntest_send_message3",
            receiveMessages = "test_receive_message1\ntest_receive_message2\ntest_receive_message3"
        )

        composeTestRule
            .onNodeWithText(fakeUiState.value.deviceInfo?.deviceId ?: "")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(fakeUiState.value.deviceInfo?.name ?: "")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(fakeUiState.value.receiveMessages)
            .assertIsDisplayed()
    }


}