package com.luxrobo.data_transfer

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.luxrobo.data_transfer.ext.ParcelableBleDeviceInfo
import com.luxrobo.data_transfer.ext.toOrigin
import com.luxrobo.data_transfer.viewModel.BleDataTransferIntent
import com.luxrobo.data_transfer.viewModel.BleDataTransferUiState
import com.luxrobo.data_transfer.viewModel.BleDataTransferViewModel
import com.luxrobo.data_transfer.viewModel.BleTransferSideEffect
import com.luxrobo.domain.usecase.GetMessageUseCase
import com.luxrobo.domain.usecase.PostMessageUseCase
import com.luxrobo.model.Message
import com.luxrobo.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals

class BleDataTransferViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val savedStateHandle =
        mockk<SavedStateHandle>(relaxed = true) // relaxed = true allows un-stubbed calls
    private val getMessageUseCase = mockk<GetMessageUseCase>()
    private val postMessageUseCase = mockk<PostMessageUseCase>()

    private lateinit var viewModel: BleDataTransferViewModel

    private val testParcelableDeviceInfo = ParcelableBleDeviceInfo(
        deviceId = "123e4567-e89b-12d3-a456-426614174000",
        name = "BLE_DEVICE_001",
        rssi = -65
    )
    private val testOriginDeviceInfo = testParcelableDeviceInfo.toOrigin()

    private val testPostMessage = Message(
        deviceId = "123e4567-e89b-12d3-a456-426614174000",
        name = "BLE_DEVICE_001",
        message = "test post message"
    )

    private val testGetMessage = Message(
        deviceId = "123e4567-e89b-12d3-a456-426614174000",
        name = "BLE_DEVICE_001",
        message = "test get message"
    )

    @Before
    fun setup() {
        every {
            savedStateHandle.getStateFlow<ParcelableBleDeviceInfo?>(
                "deviceInfo",
                null
            )
        } returns MutableStateFlow(testParcelableDeviceInfo)

        viewModel =
            BleDataTransferViewModel(savedStateHandle, getMessageUseCase, postMessageUseCase)
    }

    // --- uiState Tests ---

    @Test
    fun `uiState should be empty if no deviceInfo in SavedStateHandle`() = runTest {
        // Simulate no device info in SavedStateHandle
        every {
            savedStateHandle.getStateFlow<ParcelableBleDeviceInfo?>(
                "deviceInfo",
                null
            )
        } returns MutableStateFlow(null)
        viewModel = BleDataTransferViewModel(
            savedStateHandle,
            getMessageUseCase,
            postMessageUseCase
        ) // Re-initialize ViewModel

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(BleDataTransferUiState.empty(), initialState)
            assertNull(initialState.deviceInfo)
            assertEquals("", initialState.sendMessages)
            assertEquals("", initialState.receiveMessages)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- postMessage Tests ---
    @Test
    fun `postMessage should add message to sendMessages and update uiState`() = runTest {
        val testMsgContent = "test post message"
        coEvery { postMessageUseCase.invoke(testPostMessage) } returns flowOf(testPostMessage)
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        viewModel.uiState.test {
            val initialState = awaitItem()

            viewModel.postMessage(testMsgContent)
            val updatedUiState1 = awaitItem()
            assertEquals(updatedUiState1.sendMessages, initialState.sendMessages + testMsgContent)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `postMessage should handle multiple messages in sendMessages`() = runTest {
        val testMsgContent = "test post message"
        coEvery { postMessageUseCase.invoke(testPostMessage) } returns flowOf(testPostMessage)
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        viewModel.uiState.test {
            val initialState = awaitItem()

            viewModel.postMessage(testMsgContent)
            val updatedUiState1 = awaitItem()
            assertEquals(updatedUiState1.sendMessages, initialState.sendMessages + testMsgContent)

            viewModel.postMessage(testMsgContent)
            val updatedUiState2 = awaitItem()

            assertEquals(
                updatedUiState2.sendMessages,
                updatedUiState1.sendMessages + "\n" + testMsgContent
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- _receiveMessages and getMessageUseCase Tests ---

    @Test
    fun `receiveMessages should update uiState when getMessageUseCase emits`() = runTest {
        val testGetContent = "test get message"
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } returns flow {
            while (true) {
                emit(testGetMessage)
                delay(1000L)
            }
        }

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(testGetContent, initialState.receiveMessages)

            testScheduler.advanceTimeBy(999L)
            expectNoEvents()

            testScheduler.advanceTimeBy(1L)
            val secondUpdate = awaitItem()
            assertEquals("$testGetContent\n$testGetContent", secondUpdate.receiveMessages)

        }
    }

    // --- disconnect Tests ---

    @Test
    fun `handleIntent Disconnect should call disconnect`() = runTest {
        coEvery { postMessageUseCase.invoke(testPostMessage) } answers { flowOf(testPostMessage) }
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }


        viewModel.sideEffect.test {
            viewModel.handleIntent(BleDataTransferIntent.Disconnect)
            val sideEffect = awaitItem()
            assertEquals(BleTransferSideEffect.Finish, sideEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent PostMessage should call postMessage and update uiState`() = runTest {
        coEvery { postMessageUseCase.invoke(testPostMessage) } answers { flowOf(testPostMessage) }
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        val testMsgContent = "test post message"

        viewModel.uiState.test {
            awaitItem() // Initial state

            viewModel.handleIntent(BleDataTransferIntent.PostMessage(testMsgContent))

            val updatedState = awaitItem()
            assertEquals(testPostMessage.message, updatedState.sendMessages)
            assertEquals(testOriginDeviceInfo, updatedState.deviceInfo)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- postSideEffect Tests ---

    @Test
    fun `postSideEffect should send the given effect`() = runTest {
        coEvery { postMessageUseCase.invoke(testPostMessage) } answers { flowOf(testPostMessage) }
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        val testSideEffect = BleTransferSideEffect.Finish // Using an existing side effect type

        viewModel.sideEffect.test {
            viewModel.postSideEffect(testSideEffect)
            val receivedEffect = awaitItem()
            assertEquals(testSideEffect, receivedEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }
}