package com.luxrobo.device_list

import app.cash.turbine.test
import com.luxrobo.device_list.viewModel.BleDeviceListIntent
import com.luxrobo.device_list.viewModel.BleDeviceListSideEffect
import com.luxrobo.device_list.viewModel.BleDeviceListUiState
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
import kotlinx.coroutines.launch
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
        coEvery { getBleDeviceConnectionsUseCase.invoke() } returns flowOf(mockBleDeviceConnections)
        viewModel = BleDeviceListViewModel(getBleDeviceConnectionsUseCase)
    }

    @Test
    fun `selectDevice with good RSSI should move to detail`() = runTest {
        val goodRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedDeviceInfo = goodRssiDevice.toBleDeviceInfo()
        viewModel.uiState.test {
            awaitItem() // Initial state

            viewModel.selectDevice(goodRssiDevice)

            val connectingState = awaitItem()
            assertTrue(connectingState.bleDeviceConnections.any { it.deviceId == goodRssiDevice.deviceId  && it.isConnecting})

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.sideEffect.test {
            val sideEffect = awaitItem()
            assertEquals(BleDeviceListSideEffect.MoveToDetail(expectedDeviceInfo), sideEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selectDevice with bad RSSI should show dialog`() = runTest {
        val badRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174002",
            name = "BLE_DEVICE_003",
            rssi = -80
        ) // RSSI < -70

        viewModel.uiState.test {
            awaitItem() // initial state

            viewModel.selectDevice(badRssiDevice)
            val stateAfterBadRssi = awaitItem()
            assertTrue(stateAfterBadRssi.isDialogShowed.first)
            assertTrue(stateAfterBadRssi.isDialogShowed.second.isNotEmpty()) // 다이얼로그 메시지가 비어있지 않은지 확인
            assertFalse(stateAfterBadRssi.isConnecting) // RSSI가 나쁘면 연결 시도 안 함

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `dismissDialog should hide dialog`() = runTest {
        val badRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174002",
            name = "BLE_DEVICE_003",
            rssi = -80
        ) // RSSI < -70

        viewModel.uiState.test {
            awaitItem() // initial state

            // 먼저 다이얼로그를 표시
            viewModel.selectDevice(badRssiDevice)
            awaitItem() // dialog showed state

            viewModel.dismissDialog()
            val stateAfterDismiss = awaitItem()
            assertFalse(stateAfterDismiss.isDialogShowed.first)
            assertTrue(stateAfterDismiss.isDialogShowed.second.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `resetSelection should set selectedDeviceId to null`() = runTest {
        val device = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )

        viewModel.uiState.test {
            awaitItem() // initial state

            // 먼저 디바이스를 선택하여 _selectedDeviceId를 설정
            viewModel.selectDevice(device)
            awaitItem() // connecting state

            viewModel.resetSelection()
            val stateAfterReset = awaitItem()
            assertFalse(stateAfterReset.isConnecting)
            assertTrue(stateAfterReset.bleDeviceConnections.none { it.isConnecting })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `controlledBleDeviceFlow should emit device connections when scanning is enabled`() = runTest {
        val deviceList = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65
            ),
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174002",
                name = "BLE_DEVICE_003",
                rssi = -80
            )
        )
        // getBleDeviceConnectionsUseCase가 호출될 때 위 deviceList를 내보내도록 목킹합니다.
        coEvery { getBleDeviceConnectionsUseCase.invoke() } returns flowOf(deviceList)

        // controlledBleDeviceFlow를 test 블록으로 수집합니다.
        viewModel.controlledBleDeviceFlow().test {
            // isScanning의 초기값은 true이므로, UseCase가 호출되고 그 결과가 바로 내보내질 것입니다.
            val emittedList = awaitItem()
            assertEquals(deviceList, emittedList)

            // 더 이상 내보내질 것이 없다고 예상되면
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `controlledBleDeviceFlow should emit empty list when scanning is disabled`() = runTest {
        val deviceList = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65
            )
        )
        // UseCase가 호출되면 deviceList를 내보내도록 목킹합니다.
        coEvery { getBleDeviceConnectionsUseCase.invoke() } returns flowOf(deviceList)

        viewModel.controlledBleDeviceFlow().test {
            // 초기 상태: _isScanning은 true이므로, UseCase의 결과인 deviceList가 먼저 내보내집니다.
            val initialList = awaitItem()
            assertEquals(deviceList, initialList)

            // 이제 ViewModel의 changeScanState()를 호출하여 _isScanning을 false로 변경합니다.
            // 이로 인해 controlledBleDeviceFlow가 flatMapLatest에 의해 다시 평가됩니다.
            viewModel.changeScanState()

            // _isScanning이 false가 되었으므로, 빈 리스트가 내보내질 것입니다.
            val emptyList = awaitItem()
            assertTrue(emptyList.isEmpty())

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `controlledBleDeviceFlow should stop emitting from use case when scanning is disabled and then re-enable scanning`() = runTest {
        val deviceList1 = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174000",
                name = "BLE_DEVICE_001",
                rssi = -65
            )
        )
        val deviceList2 = listOf(
            BleDeviceConnection(
                deviceId = "123e4567-e89b-12d3-a456-426614174002",
                name = "BLE_DEVICE_003",
                rssi = -80
            )
        )
        // UseCase가 호출될 때마다 다른 Flow를 내보내도록 시퀀스를 목킹할 수 있습니다.
        // 하지만 여기서는 간단히 첫 번째 호출에만 특정 리스트를 반환하도록 합니다.
        // 실제 UseCase는 계속해서 새로운 데이터를 내보낼 수 있으므로,
        // 이 테스트는 _isScanning 변화에 따른 flatMapLatest의 동작을 검증하는 데 초점을 맞춥니다.
        coEvery { getBleDeviceConnectionsUseCase.invoke() } returnsMany listOf(flowOf(deviceList1), flowOf(deviceList2))


        viewModel.controlledBleDeviceFlow().test {
            // 1. 초기 상태: isScanning = true. UseCase로부터 첫 번째 리스트를 받습니다.
            val initialList = awaitItem()
            assertEquals(deviceList1, initialList)

            // 2. 스캔 비활성화: _isScanning이 false로 변경됩니다.
            viewModel.changeScanState()

            // flatMapLatest에 의해 Flow가 전환되고 빈 리스트가 내보내집니다.
            val emptyList = awaitItem()
            assertTrue(emptyList.isEmpty())

            // 3. 스캔 재활성화: _isScanning이 다시 true로 변경됩니다.
            viewModel.changeScanState()

            // flatMapLatest에 의해 UseCase가 다시 호출되고 두 번째 리스트가 내보내집니다.
            val reEnabledList = awaitItem()
            assertEquals(deviceList2, reEnabledList)

            expectNoEvents() // 더 이상 이벤트가 없음을 예상
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `postSideEffect should send the correct side effect`() = runTest {
        val testDeviceInfo =  BleDeviceInfo(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedSideEffect = BleDeviceListSideEffect.MoveToDetail(testDeviceInfo)

        viewModel.sideEffect.test {
            // Act: Post a side effect
            viewModel.postSideEffect(expectedSideEffect)

            // Assert: Verify that the side effect was received
            val receivedEffect = awaitItem()
            assertEquals(expectedSideEffect, receivedEffect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent ChangeScanState should toggle isScanning`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Initial state: isScanning = true

            // Act: Send ChangeScanState intent
            viewModel.handleIntent(BleDeviceListIntent.ChangeScanState)

            // Assert: isScanning should be false
            val stateAfterIntent = awaitItem()
            assertFalse(stateAfterIntent.isScanning)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent DismissDialog should dismiss the dialog`() = runTest {
        val badRssiDeviceConnection = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -75 // RSSI를 -70 미만으로 설정하여 다이얼로그가 표시되도록 유도
        )

        viewModel.uiState.test {
            awaitItem() // Initial state

            // First, make the dialog show up
            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(badRssiDeviceConnection))

            val stateWithDialogShown = awaitItem()
            assertTrue(stateWithDialogShown.isDialogShowed.first)
            assertTrue(stateWithDialogShown.isDialogShowed.second.isNotEmpty())

            // Act: Send DismissDialog intent
            viewModel.handleIntent(BleDeviceListIntent.DismissDialog)

            // Assert: Dialog should be hidden
            val stateAfterDismiss = awaitItem()
            assertFalse(stateAfterDismiss.isDialogShowed.first)
            assertTrue(stateAfterDismiss.isDialogShowed.second.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent SelectDevice with good RSSI should post MoveToDetail side effect`() = runTest {
        val goodRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )
        val expectedDeviceInfo = goodRssiDevice.toBleDeviceInfo()

        viewModel.sideEffect.test {
            // Act: Send SelectDevice intent with good RSSI
            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(goodRssiDevice))

            // Assert: Side effect should be received
            val receivedEffect = awaitItem()
            assertEquals(BleDeviceListSideEffect.MoveToDetail(expectedDeviceInfo), receivedEffect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent SelectDevice with bad RSSI should show dialog`() = runTest {
        val badRssiDevice = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -80
        )

        viewModel.uiState.test {
            awaitItem() // Initial state

            // Act: Send SelectDevice intent with bad RSSI
            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(badRssiDevice))

            // Assert: Dialog should be shown
            val stateAfterBadRssi = awaitItem()
            assertTrue(stateAfterBadRssi.isDialogShowed.first)
            assertTrue(stateAfterBadRssi.isDialogShowed.second.isNotEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent ResetSelection should reset selection`() = runTest {
        val device = BleDeviceConnection(
            deviceId = "123e4567-e89b-12d3-a456-426614174000",
            name = "BLE_DEVICE_001",
            rssi = -65
        )

        viewModel.uiState.test {
            awaitItem() // Initial state

            // First, select a device to set _selectedDeviceId
            viewModel.handleIntent(BleDeviceListIntent.SelectDevice(device))
            awaitItem() // Connecting state

            // Act: Send ResetSelection intent
            viewModel.handleIntent(BleDeviceListIntent.ResetSelection)

            // Assert: isConnecting should be false and no device should be selected
            val state = awaitItem()
            assertFalse(state.isConnecting)
            assertTrue(state.bleDeviceConnections.none { it.isConnecting })

            cancelAndIgnoreRemainingEvents()
        }
    }
}