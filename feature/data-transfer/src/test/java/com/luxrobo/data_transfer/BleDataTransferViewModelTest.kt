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

    private val savedStateHandle = mockk<SavedStateHandle>()
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

    @Test
    fun `SavedStateHandle에 deviceInfo가 없으면 uiState는 비어있어야 한다`() = runTest {
        // Given
        every {
            savedStateHandle.getStateFlow<ParcelableBleDeviceInfo?>(
                "deviceInfo",
                null
            )
        } returns MutableStateFlow(null)

        // When
        viewModel = BleDataTransferViewModel(
            savedStateHandle,
            getMessageUseCase,
            postMessageUseCase
        )

        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(BleDataTransferUiState.empty(), initialState)
            assertNull(initialState.deviceInfo)
            assertEquals("", initialState.sendMessages)
            assertEquals("", initialState.receiveMessages)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `postMessage 호출 시 sendMessages에 메시지가 추가되고 uiState가 갱신된다`() = runTest {
        // Given
        val testMsgContent = "test post message"
        coEvery { postMessageUseCase.invoke(testPostMessage) } returns flowOf(testPostMessage)
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        viewModel.uiState.test {
            val initialState = awaitItem()

            // When
            viewModel.postMessage(testMsgContent)

            // Then
            val updatedUiState1 = awaitItem()
            assertEquals(updatedUiState1.sendMessages, initialState.sendMessages + testMsgContent)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `postMessage가 여러 번 호출되면 sendMessages에 줄바꿈으로 누적된다`() = runTest {
        // Given
        val testMsgContent = "test post message"
        coEvery { postMessageUseCase.invoke(testPostMessage) } returns flowOf(testPostMessage)
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        viewModel.uiState.test {
            val initialState = awaitItem()

            // When
            viewModel.postMessage(testMsgContent)
            val updatedUiState1 = awaitItem()

            viewModel.postMessage(testMsgContent)
            val updatedUiState2 = awaitItem()

            // Then
            assertEquals(updatedUiState1.sendMessages, initialState.sendMessages + testMsgContent)
            assertEquals(
                updatedUiState2.sendMessages,
                updatedUiState1.sendMessages + "\n" + testMsgContent
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getMessageUseCase가 emit하면 receiveMessages가 갱신된다`() = runTest {
        // Given
        val testGetContent = "test get message"
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } returns flow {
            while (true) {
                emit(testGetMessage)
                delay(1000L)
            }
        }

        // When & Then
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


    @Test
    fun `handleIntent가 Disconnect면 disconnect 동작이 수행된다`() = runTest {
        // Given
        coEvery { postMessageUseCase.invoke(testPostMessage) } answers { flowOf(testPostMessage) }
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        // When
        viewModel.sideEffect.test {
            viewModel.handleIntent(BleDataTransferIntent.Disconnect)

            // Then
            val sideEffect = awaitItem()
            assertEquals(BleTransferSideEffect.Finish, sideEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent가 PostMessage면 postMessage가 호출되고 uiState가 갱신된다`() = runTest {
        // Given
        coEvery { postMessageUseCase.invoke(testPostMessage) } answers { flowOf(testPostMessage) }
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        val testMsgContent = "test post message"

        viewModel.uiState.test {
            awaitItem() // Initial state

            // When
            viewModel.handleIntent(BleDataTransferIntent.PostMessage(testMsgContent))

            // Then
            val updatedState = awaitItem()
            assertEquals(testPostMessage.message, updatedState.sendMessages)
            assertEquals(testOriginDeviceInfo, updatedState.deviceInfo)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `postSideEffect가 호출되면 해당 사이드이펙트가 전송된다`() = runTest {
        // Given
        coEvery { postMessageUseCase.invoke(testPostMessage) } answers { flowOf(testPostMessage) }
        coEvery { getMessageUseCase.invoke(testOriginDeviceInfo) } answers { flowOf(testGetMessage) }

        val testSideEffect = BleTransferSideEffect.Finish

        // When
        viewModel.sideEffect.test {
            viewModel.postSideEffect(testSideEffect)

            // Then
            val receivedEffect = awaitItem()
            assertEquals(testSideEffect, receivedEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }
}