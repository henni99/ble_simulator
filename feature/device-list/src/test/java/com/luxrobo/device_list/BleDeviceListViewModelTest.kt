package com.luxrobo.device_list

import app.cash.turbine.test
import com.luxrobo.device_list.viewModel.BleDeviceListIntent
import com.luxrobo.device_list.viewModel.BleDeviceListSideEffect
import com.luxrobo.device_list.viewModel.BleDeviceListViewModel
import com.luxrobo.domain.usecase.GetBleDeviceConnectionsUseCase
import com.luxrobo.mapper.toBleDeviceInfo
import com.luxrobo.mock.mockBleDeviceConnections
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class BleDeviceListViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getBleDeviceConnectionsUseCase = mockk<GetBleDeviceConnectionsUseCase>()

    private lateinit var viewModel: BleDeviceListViewModel

    @Before
    fun setup() {
        coEvery { getBleDeviceConnectionsUseCase() } returns flowOf(mockBleDeviceConnections)
        viewModel = BleDeviceListViewModel(getBleDeviceConnectionsUseCase)
    }

    @Test
    fun `-RSSI가_75이상 일때_RSSI_기기의_선택은_상세화면으로_이동한다`() = runTest {
        // Given
        val goodRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedDeviceInfo = goodRssiDevice.toBleDeviceInfo()

        viewModel.uiState.test {
            awaitItem()

            // When
            viewModel.selectDevice(goodRssiDevice)

            // Then
            val connectingState = awaitItem()
            assertTrue(connectingState.bleDeviceConnections.any { it.deviceId == goodRssiDevice.deviceId && it.isConnecting })

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.sideEffect.test {
            val sideEffect = awaitItem()
            assertEquals(BleDeviceListSideEffect.MoveToDetail(expectedDeviceInfo), sideEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `RSSI가_-75미만_일때_기기의_Dialog를_표시한다`() = runTest {
        val badRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456174002",
            name = "BLE_DEVICE_003",
            rssi = -80
        )

        viewModel.uiState.test {
            awaitItem()

            // When
            viewModel.selectDevice(badRssiDevice)

            // Then
            val stateAfterBadRssi = awaitItem()
            assertTrue(stateAfterBadRssi.isDialogShowed.first)
            assertTrue(stateAfterBadRssi.isDialogShowed.second.isNotEmpty())
            assertFalse(stateAfterBadRssi.isConnecting)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Dialog를_없애면_실제로_Dialog가_없어진다`() = runTest {
        val badRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456174002",
            name = "BLE_DEVICE_003",
            rssi = -80
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.selectDevice(badRssiDevice)
            awaitItem()

            // When
            viewModel.dismissDialog()

            // Then
            val stateAfterDismiss = awaitItem()
            assertFalse(stateAfterDismiss.isDialogShowed.first)
            assertTrue(stateAfterDismiss.isDialogShowed.second.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `선택을_해제되면_isConnecting은_false로_설정한다`() = runTest {
        val device = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.selectDevice(device)
            awaitItem()

            // When
            viewModel.resetSelection()

            // Then
            val stateAfterReset = awaitItem()
            assertFalse(stateAfterReset.isConnecting)
            assertTrue(stateAfterReset.bleDeviceConnections.none { it.isConnecting })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `postSideEffect를_호출하면_viewModel의_sideEffect에_전달한다`() = runTest {
        val testDeviceInfo = BleDeviceInfo(
            deviceId = "123e4567-e89b-12d3-a456174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedSideEffect = BleDeviceListSideEffect.MoveToDetail(testDeviceInfo)

        viewModel.sideEffect.test {
            // When
            viewModel.postSideEffect(expectedSideEffect)

            // Then
            val receivedEffect = awaitItem()
            assertEquals(expectedSideEffect, receivedEffect)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `ChangeScanState_인텐트_처리는_isScanning_상태를_변경한다`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            // When
            viewModel.handleIntent(BleDeviceListIntent.ChangeScanState)

            // Then
            val stateAfterIntent = awaitItem()
            assertFalse(stateAfterIntent.isScanning)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `DismissDialog_인텐트_처리는_Dialog를_숨긴다`() = runTest {
        val badRssiDeviceConnection = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456174000",
            name = "BLE_DEVICE_001",
            rssi = -80
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(badRssiDeviceConnection))

            val stateWithDialogShown = awaitItem()
            assertTrue(stateWithDialogShown.isDialogShowed.first)
            assertTrue(stateWithDialogShown.isDialogShowed.second.isNotEmpty())

            // When
            viewModel.handleIntent(BleDeviceListIntent.DismissDialog)

            // Then
            val stateAfterDismiss = awaitItem()
            assertFalse(stateAfterDismiss.isDialogShowed.first)
            assertTrue(stateAfterDismiss.isDialogShowed.second.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `RSSI가_-75이상인_기기_선택_인텐트_처리는_MoveToDetail_SideEffect를_전송한다`() = runTest {
        val goodRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedDeviceInfo = goodRssiDevice.toBleDeviceInfo()

        viewModel.sideEffect.test {
            // When
            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(goodRssiDevice))

            // Then
            val receivedEffect = awaitItem()
            assertEquals(BleDeviceListSideEffect.MoveToDetail(expectedDeviceInfo), receivedEffect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `RSSI가_-75미만인_기기_선택_인텐트_처리는_Dialog를_보여준다`() = runTest {
        val badRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456174000",
            name = "BLE_DEVICE_001",
            rssi = -80
        )

        viewModel.uiState.test {
            awaitItem()

            // When
            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(badRssiDevice))

            // Then
            val stateAfterBadRssi = awaitItem()
            assertTrue(stateAfterBadRssi.isDialogShowed.first)
            assertTrue(stateAfterBadRssi.isDialogShowed.second.isNotEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ResetSelection_인텐트_처리는_기기_선택을_초기화한다`() = runTest {
        val device = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(device))
            awaitItem()

            // When
            viewModel.handleIntent(BleDeviceListIntent.ResetSelection)

            // Then
            val state = awaitItem()
            assertFalse(state.isConnecting)
            assertTrue(state.bleDeviceConnections.none { it.isConnecting })

            cancelAndIgnoreRemainingEvents()
        }
    }
}